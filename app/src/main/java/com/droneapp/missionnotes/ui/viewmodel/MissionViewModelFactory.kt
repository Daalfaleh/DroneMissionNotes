package com.droneapp.missionnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droneapp.missionnotes.data.repository.MissionRepository

class MissionViewModelFactory(
    private val repository: MissionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MissionViewModel::class.java)) {
            return MissionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}