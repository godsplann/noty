package com.cker.noty.ui.addorupdatenote

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cker.noty.data.model.Note
import com.cker.noty.ui.theme.NotyTypography
import kotlinx.serialization.Serializable

@Serializable
data class AddOrUpdateNote(
    val noteId: Int?,
)

@Composable
fun AddOrUpdateNoteRoute(
    navController: NavController = rememberNavController(),
    addOrUpdateNoteViewModel: AddOrUpdateNoteViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            addOrUpdateNoteViewModel.effects.collect {
                when (it) {
                    is AddOrUpdateNoteEffect.NavigateToNoteList -> {
                        navController.popBackStack()
                    }

                    is AddOrUpdateNoteEffect.ShowError -> {
                        Log.e("AddOrUpdateNoteRoute", it.error)
                    }

                    AddOrUpdateNoteEffect.NavigateBack -> navController.popBackStack()
                }
            }
        }
    }

    val uiState by addOrUpdateNoteViewModel.state.collectAsStateWithLifecycle()

    AddOrUpdateNoteContent(uiState, addOrUpdateNoteViewModel::onEvent)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateNoteContent(
    uiState: AddOrUpdateNoteState,
    onEvent: (AddOrUpdateNoteEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                title = {
                    Text(
                        text = if (uiState.isEditing) "Update Note" else "Add Note",
                        style = NotyTypography.h2
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onEvent(AddOrUpdateNoteEvent.OnBackPressed)
                            }
                    )
                },
                actions = {
                    Button(
                        onClick = { onEvent(AddOrUpdateNoteEvent.OnNoteSaved) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = if (uiState.isEditing) "Update" else "Save",
                            style = NotyTypography.h4,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(innerPadding)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.note.title,
                onValueChange = {
                    onEvent(AddOrUpdateNoteEvent.OnTitleChanged(it))
                },
                label = { Text("Title", style = NotyTypography.h4, color = Color.LightGray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                )
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.note.content,
                onValueChange = {
                    onEvent(AddOrUpdateNoteEvent.OnContentChanged(it))
                },
                label = { Text("Description", style = NotyTypography.h4, color = Color.LightGray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                )
            )
        }
    }
}

@Preview
@Composable
fun AddOrUpdateNotePreview() {
    AddOrUpdateNoteContent(
        uiState = AddOrUpdateNoteState(
            note = Note(
                title = "This is the note title",
                content = "This is the content of the note that is being viewed",
                createdAt = 0L, updatedAt = 0L
            ),
        )
    ) { }
}