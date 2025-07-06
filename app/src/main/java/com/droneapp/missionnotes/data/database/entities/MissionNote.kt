package com.droneapp.missionnotes.data.database.entities

import java.util.UUID

data class MissionNote(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val location: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
            description,
            location,
            "$title $description",
            "$title $location",
            "$description $location"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}