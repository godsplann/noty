package com.cker.noty.ui.theme

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cker.noty.data.Note
import kotlinx.serialization.Serializable


@Serializable
object ListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    notes: List<Note> = emptyList()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") }
            )
        },
        floatingActionButton = {
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note"
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(notes) { note ->
                NoteListItem(note = note)
            }
        }
    }
}

@Composable
fun NoteListItem(modifier: Modifier = Modifier,note: Note) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = note.title)
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen(
        listOf(
            Note(
                title = "Title 1",
                content = "Content 2",
                createdAt = 12415,
                updatedAt = 1526347
            ),
            Note(
                title = "Title 2",
                content = "Content 2",
                createdAt = 12415,
                updatedAt = 1526347
            ),
        )
    )
}