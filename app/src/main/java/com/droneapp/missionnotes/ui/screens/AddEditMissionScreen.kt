package com.droneapp.missionnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.droneapp.missionnotes.data.database.entities.MissionNote
import com.droneapp.missionnotes.ui.components.LocationPicker
import com.droneapp.missionnotes.ui.viewmodel.MissionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMissionScreen(
    viewModel: MissionViewModel,
    noteId: String?,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var existingNote by remember { mutableStateOf<MissionNote?>(null) }

    val scope = rememberCoroutineScope()
    val isEditing = noteId != null

    // Load existing note if editing
    LaunchedEffect(noteId) {
        if (noteId != null) {
            isLoading = true
            try {
                val note = viewModel.getNoteById(noteId)
                if (note != null) {
                    existingNote = note
                    title = note.title
                    description = note.description
                    location = note.location
                }
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Mission" else "New Mission",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (title.isNotBlank()) {
                                    if (isEditing && existingNote != null) {
                                        // Update existing note
                                        val updatedNote = existingNote!!.copy(
                                            title = title.trim(),
                                            description = description.trim(),
                                            location = location.trim()
                                        )
                                        viewModel.updateNote(updatedNote)
                                    } else {
                                        // Create new note
                                        val newNote = MissionNote(
                                            title = title.trim(),
                                            description = description.trim(),
                                            location = location.trim()
                                        )
                                        viewModel.insertNote(newNote)
                                    }
                                    onNavigateBack()
                                }
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title field
                Column {
                    Text(
                        text = "Mission Title *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Enter mission title",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        singleLine = true,
                        isError = title.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                    if (title.isBlank()) {
                        Text(
                            text = "Title is required",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Description field
                Column {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            Text(
                                text = "Enter mission description, objectives, notes...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                }

                // Location field
                LocationPicker(
                    location = location,
                    onLocationChange = { location = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save button (alternative to top bar)
                Button(
                    onClick = {
                        scope.launch {
                            if (title.isNotBlank()) {
                                if (isEditing && existingNote != null) {
                                    // Update existing note
                                    val updatedNote = existingNote!!.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        location = location.trim()
                                    )
                                    viewModel.updateNote(updatedNote)
                                } else {
                                    // Create new note
                                    val newNote = MissionNote(
                                        title = title.trim(),
                                        description = description.trim(),
                                        location = location.trim()
                                    )
                                    viewModel.insertNote(newNote)
                                }
                                onNavigateBack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditing) "Update Mission" else "Save Mission",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Cancel button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Bottom spacing for better UX
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}