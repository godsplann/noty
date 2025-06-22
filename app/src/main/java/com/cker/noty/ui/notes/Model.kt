package com.cker.noty.ui.notes

import androidx.compose.runtime.Immutable
import com.cker.noty.data.model.Note

@Immutable
data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface NoteListEvent {
    data object OnAddNoteClicked : NoteListEvent
    data class OnNoteClicked(val noteId: Int) : NoteListEvent
    data class OnDeleteNoteClicked(val noteId: Int) : NoteListEvent
}

sealed interface NoteListEffect {
    data class NavigateToAddNote(val noteId: Int?) : NoteListEffect
}