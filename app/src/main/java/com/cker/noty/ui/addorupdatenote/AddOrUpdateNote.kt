package com.cker.noty.ui.addorupdatenote

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
data class AddOrUpdateNote(
    val noteId: Int?,
)

@OptIn(ExperimentalMaterial3Api::class)
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
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Note")
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val state = addOrUpdateNoteViewModel.state.collectAsState().value
            TextField(
                value = state.title,
                onValueChange = {
                    addOrUpdateNoteViewModel.onEvent(AddOrUpdateNoteEvent.OnTitleChanged(it))
                },
                label = { Text("Title") }
            )

            TextField(
                value = state.description,
                onValueChange = {
                    addOrUpdateNoteViewModel.onEvent(AddOrUpdateNoteEvent.OnDescriptionChanged(it))
                },
                label = { Text("Description") }
            )

            Text(
                text = "Save",
                modifier = Modifier.padding(top = 16.dp).clickable {
                    addOrUpdateNoteViewModel.onEvent(AddOrUpdateNoteEvent.OnNoteSaved)
                }
            )
        }
    }
}