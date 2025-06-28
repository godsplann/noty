package com.cker.noty.ui.addorupdatenote

import androidx.compose.runtime.Immutable
import com.cker.noty.data.model.Note

@Immutable
data class AddOrUpdateNoteState(
    val note: Note = Note(
        title = "",
        content = "",
        createdAt = 0,
        updatedAt = 0
    ),
    val isEditing: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface AddOrUpdateNoteEvent {
    data object OnBackPressed : AddOrUpdateNoteEvent
    data class OnTitleChanged(val title: String) : AddOrUpdateNoteEvent
    data class OnContentChanged(val content: String) : AddOrUpdateNoteEvent
    data object OnNoteSaved : AddOrUpdateNoteEvent
    data class OnNoteSaveFailed(val error: String) : AddOrUpdateNoteEvent
}

sealed interface AddOrUpdateNoteEffect {
    data object NavigateToNoteList : AddOrUpdateNoteEffect
    data object NavigateBack : AddOrUpdateNoteEffect
    data class ShowError(val error: String) : AddOrUpdateNoteEffect
}