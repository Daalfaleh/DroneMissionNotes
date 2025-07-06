package com.droneapp.missionnotes.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationHelper(private val context: Context) {

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission() || !isLocationEnabled()) {
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // Try to get last known location from GPS first, then network
                val gpsLocation = try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } catch (e: SecurityException) {
                    null
                }

                val networkLocation = try {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } catch (e: SecurityException) {
                    null
                }

                // Choose the most recent location
                val bestLocation = when {
                    gpsLocation != null && networkLocation != null -> {
                        if (gpsLocation.time > networkLocation.time) gpsLocation else networkLocation
                    }
                    gpsLocation != null -> gpsLocation
                    networkLocation != null -> networkLocation
                    else -> null
                }

                continuation.resume(bestLocation)
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }
    }

    fun getLocationUpdates(interval: Long = 10000L): Flow<Location> = callbackFlow {
        if (!hasLocationPermission() || !isLocationEnabled()) {
            close()
            return@callbackFlow
        }

        // For this simplified version, we'll just emit the current location once
        // In a real app, you'd implement continuous location updates
        val currentLocation = getCurrentLocation()
        if (currentLocation != null) {
            trySend(currentLocation)
        }

        awaitClose { /* Cleanup if needed */ }
    }

    fun formatLocationString(location: Location): String {
        val latitude = String.format("%.6f", location.latitude)
        val longitude = String.format("%.6f", location.longitude)
        return "$latitude, $longitude"
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
    }
}