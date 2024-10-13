package com.cker.noty.ui.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cker.noty.data.model.Note
import com.cker.noty.ui.addorupdatenote.AddOrUpdateNote
import kotlinx.serialization.Serializable


@Serializable
object ListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(
    navController: NavController = rememberNavController(),
    noteListViewModel: NoteListViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            noteListViewModel.effects.collect {
                when (it) {
                    is NoteListEffect.NavigateToAddNote -> {
                        navController.navigate(AddOrUpdateNote(it.noteId))
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") }
            )
        },
        floatingActionButton = {
            Icon(
                modifier = Modifier.clickable {
                    noteListViewModel.onEvent(NoteListEvent.OnAddNoteClicked)
                }.padding(16.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note"
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        val uiState = noteListViewModel.state.collectAsState()
        if (uiState.value.notes.isEmpty()) {
            Text("No notes found")
        } else {
            NotesListContent(Modifier.padding(innerPadding), uiState.value.notes)
        }
    }
}

@Composable
fun NotesListContent(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList()
) {
    LazyColumn(modifier = modifier) {
        items(notes) { note ->
            NoteListItem(note = note)
        }
    }
}

@Composable
fun NoteListItem(modifier: Modifier = Modifier, note: Note) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = note.title)
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    NotesListContent(
        notes = listOf(
            Note(
                id = 1,
                title = "Title 1",
                content = "Content 2",
                createdAt = 12415,
                updatedAt = 1526347
            ),
            Note(
                id = 2,
                title = "Title 2",
                content = "Content 2",
                createdAt = 12415,
                updatedAt = 1526347
            ),
        )
    )
}