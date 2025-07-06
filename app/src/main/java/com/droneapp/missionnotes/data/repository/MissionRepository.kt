package com.droneapp.missionnotes.data.repository

import com.droneapp.missionnotes.data.database.SimpleDataStore
import com.droneapp.missionnotes.data.database.entities.MissionNote
import kotlinx.coroutines.flow.Flow

class MissionRepository(private val dataStore: SimpleDataStore) {

    fun getAllNotes(): Flow<List<MissionNote>> = dataStore.getAllNotes()

    suspend fun getNoteById(id: String): MissionNote? = dataStore.getNoteById(id)

    fun searchNotes(query: String): Flow<List<MissionNote>> {
        // For simplicity, we'll filter in the ViewModel
        return dataStore.getAllNotes()
    }

    fun getPinnedNotes(): Flow<List<MissionNote>> = dataStore.getAllNotes()

    suspend fun insertNote(note: MissionNote) = dataStore.insertNote(note)

    suspend fun updateNote(note: MissionNote) = dataStore.updateNote(note)

    suspend fun deleteNote(note: MissionNote) = dataStore.deleteNote(note)

    suspend fun deleteAllNotes() = dataStore.deleteAllNotes()

    suspend fun updatePinStatus(id: String, isPinned: Boolean) = dataStore.updatePinStatus(id, isPinned)

    suspend fun getNotesCount(): Int = dataStore.getNotesCount()
}