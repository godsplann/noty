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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val uiState by noteListViewModel.state.collectAsStateWithLifecycle()


    NotesListContent(
        uiState,
        noteListViewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListContent(
    uiState: NoteListState,
    onEvent: (NoteListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                title = {
                    Text(
                        text = "Notes",
                        style = NotyTypography.h2,
                        color = Color.Black
                    )
                }
            )
        },
        floatingActionButton = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable { onEvent(NoteListEvent.OnAddNoteClicked) }
                    .padding(16.dp),
                tint = Color.White,
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note"
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            if (uiState.notes.isEmpty()) {
                Text("No notes found")
                return@Scaffold
            }
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.LightGray)
                    .fillMaxSize()
            ) {
                items(uiState.notes) { note ->
                    NoteListItem(note = note, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
fun NoteListItem(
    modifier: Modifier = Modifier,
    note: Note,
    onEvent: (NoteListEvent) -> Unit
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEvent(NoteListEvent.OnNoteClicked(note.id)) }
            .padding(12.dp),
        text = note.title,
        color = Color.DarkGray,
        style = NotyTypography.h5
    )
}


@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    NotesListContent(
        uiState = NoteListState(
            listOf(
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
        ),
        onEvent = {}
    )
}