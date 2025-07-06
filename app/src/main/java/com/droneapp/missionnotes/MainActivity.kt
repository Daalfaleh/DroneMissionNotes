package com.droneapp.missionnotes

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droneapp.missionnotes.data.database.SimpleDataStore
import com.droneapp.missionnotes.data.repository.MissionRepository
import com.droneapp.missionnotes.ui.screens.AddEditMissionScreen
import com.droneapp.missionnotes.ui.screens.MissionListScreen
import com.droneapp.missionnotes.ui.screens.SettingsScreen
import com.droneapp.missionnotes.ui.theme.DroneMissionNotesTheme
import com.droneapp.missionnotes.ui.viewmodel.MissionViewModel
import com.droneapp.missionnotes.ui.viewmodel.MissionViewModelFactory

class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request location permissions
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            DroneMissionNotesTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create simple data store and repository
                    val dataStore = remember { SimpleDataStore() }
                    val repository = remember { MissionRepository(dataStore) }

                    // Create ViewModel
                    val viewModel: MissionViewModel = viewModel(
                        factory = MissionViewModelFactory(repository)
                    )

                    // Navigation
                    val navController = rememberNavController()

                    DroneAppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}

@Composable
fun DroneAppNavigation(
    navController: NavHostController,
    viewModel: MissionViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "mission_list"
    ) {
        composable("mission_list") {
            MissionListScreen(
                viewModel = viewModel,
                onNavigateToAddEdit = { noteId ->
                    if (noteId != null) {
                        navController.navigate("add_edit_mission/$noteId")
                    } else {
                        navController.navigate("add_edit_mission")
                    }
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("add_edit_mission") {
            AddEditMissionScreen(
                viewModel = viewModel,
                noteId = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("add_edit_mission/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            AddEditMissionScreen(
                viewModel = viewModel,
                noteId = noteId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
    }
}