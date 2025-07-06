package com.droneapp.missionnotes.data.database

import com.droneapp.missionnotes.data.database.entities.MissionNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SimpleDataStore {
    private val _notes = MutableStateFlow<List<MissionNote>>(emptyList())

    fun getAllNotes(): Flow<List<MissionNote>> = _notes.asStateFlow()

    suspend fun getNoteById(id: String): MissionNote? {
        return _notes.value.find { it.id == id }
    }

    suspend fun insertNote(note: MissionNote) {
        val currentNotes = _notes.value.toMutableList()
        currentNotes.add(note)
        // Sort by pinned first, then by timestamp descending
        _notes.value = currentNotes.sortedWith(
            compareByDescending<MissionNote> { it.isPinned }
                .thenByDescending { it.timestamp }
        )
    }

    suspend fun updateNote(note: MissionNote) {
        val currentNotes = _notes.value.toMutableList()
        val index = currentNotes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            currentNotes[index] = note
            // Sort by pinned first, then by timestamp descending
            _notes.value = currentNotes.sortedWith(
                compareByDescending<MissionNote> { it.isPinned }
                    .thenByDescending { it.timestamp }
            )
        }
    }

    suspend fun deleteNote(note: MissionNote) {
        val currentNotes = _notes.value.toMutableList()
        currentNotes.removeAll { it.id == note.id }
        _notes.value = currentNotes
    }

    suspend fun updatePinStatus(id: String, isPinned: Boolean) {
        val currentNotes = _notes.value.toMutableList()
        val index = currentNotes.indexOfFirst { it.id == id }
        if (index != -1) {
            currentNotes[index] = currentNotes[index].copy(isPinned = isPinned)
            // Sort by pinned first, then by timestamp descending
            _notes.value = currentNotes.sortedWith(
                compareByDescending<MissionNote> { it.isPinned }
                    .thenByDescending { it.timestamp }
            )
        }
    }

    suspend fun deleteAllNotes() {
        _notes.value = emptyList()
    }

    suspend fun getNotesCount(): Int {
        return _notes.value.size
    }
}