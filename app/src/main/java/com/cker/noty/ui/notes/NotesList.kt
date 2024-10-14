package com.cker.noty.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cker.myapplication.R
import com.cker.noty.data.model.Note
import com.cker.noty.ui.addorupdatenote.AddOrUpdateNote
import com.cker.noty.ui.theme.NotyTypography
import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
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
    val uiState = noteListViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes",
                        style = NotyTypography.h2,
                        color = Color.Gray
                    )
                }
            )
        },
        floatingActionButton = {
            Icon(
                modifier = Modifier
                    .clickable { noteListViewModel.onEvent(NoteListEvent.OnAddNoteClicked) }
                    .padding(16.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note"
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        if (uiState.value.isLoading) {
            CircularProgressIndicator()
        } else {
            NotesListContent(
                Modifier.padding(innerPadding),
                uiState.value.notes,
                noteListViewModel::onEvent,
            )
        }
    }
}

@Composable
fun NotesListContent(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
    onEvent: (NoteListEvent) -> Unit
) {
    if (notes.isEmpty()) {
        Text("No notes found")
        return
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        items(notes) { note ->
            NoteListItem(note = note, onEvent = onEvent)
            HorizontalDivider(modifier = Modifier.background(Color.White))
        }
    }
}

@Composable
fun NoteListItem(
    modifier: Modifier = Modifier,
    note: Note,
    onEvent: (NoteListEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onEvent(NoteListEvent.OnNoteClicked(note.id)) },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = note.title, color = Color.White, style = NotyTypography.h5)
        Icon(
            painter = painterResource(R.drawable.baseline_delete_24),
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.clickable { onEvent(NoteListEvent.OnDeleteNoteClicked(note.id)) }
        )
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
        ),
        onEvent = {}
    )
}