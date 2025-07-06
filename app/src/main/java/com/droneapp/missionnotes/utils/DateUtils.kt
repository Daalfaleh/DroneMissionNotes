package com.droneapp.missionnotes.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateUtils {

    private const val DATE_FORMAT = "MMM dd, yyyy"
    private const val TIME_FORMAT = "HH:mm"
    private const val DATETIME_FORMAT = "MMM dd, yyyy 'at' HH:mm"

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    fun getRelativeTimeString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val difference = now - timestamp

        return when {
            difference < 60_000 -> "Just now"
            difference < 3_600_000 -> "${difference / 60_000} min ago"
            difference < 86_400_000 -> "${difference / 3_600_000} hours ago"
            difference < 604_800_000 -> "${difference / 86_400_000} days ago"
            else -> formatDate(timestamp)
        }
    }

    fun isToday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val todayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return today == targetDay && todayYear == targetYear
    }

    fun isYesterday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val todayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return (today - targetDay == 1) && (todayYear == targetYear)
    }

    fun getDisplayTime(timestamp: Long): String {
        return when {
            isToday(timestamp) -> "Today, ${formatTime(timestamp)}"
            isYesterday(timestamp) -> "Yesterday, ${formatTime(timestamp)}"
            else -> formatDateTime(timestamp)
        }
    }
}