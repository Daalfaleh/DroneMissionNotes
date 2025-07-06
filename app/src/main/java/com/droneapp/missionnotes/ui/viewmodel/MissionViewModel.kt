package com.droneapp.missionnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droneapp.missionnotes.data.database.entities.MissionNote
import com.droneapp.missionnotes.data.repository.MissionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MissionViewModel(
    private val repository: MissionRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _notes = MutableStateFlow<List<MissionNote>>(emptyList())
    val notes = searchText
        .combine(_notes) { text, notes ->
            if (text.isBlank()) {
                notes
            } else {
                notes.filter { it.doesMatchSearchQuery(text) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _notes.value
        )

    init {
        viewModelScope.launch {
            repository.getAllNotes().collect {
                _notes.value = it
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            _searchText.value = ""
        }
    }

    fun insertNote(note: MissionNote) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: MissionNote) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: MissionNote) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun togglePinStatus(noteId: String, isPinned: Boolean) {
        viewModelScope.launch {
            repository.updatePinStatus(noteId, !isPinned)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAllNotes()
        }
    }

    suspend fun getNoteById(id: String): MissionNote? {
        return repository.getNoteById(id)
    }
}