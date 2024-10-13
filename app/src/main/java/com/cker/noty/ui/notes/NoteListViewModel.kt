package com.cker.noty.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cker.noty.data.model.Note
import com.cker.noty.domain.usecase.NoteCrudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteCrudUseCase: NoteCrudUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state

    private val _effects = Channel<NoteListEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            _state.value = _state.value.copy(notes = noteCrudUseCase.getNotes(), isLoading = false)
        }
    }

    fun onEvent(event: NoteListEvent) {
        viewModelScope.launch {
            when (event) {
                is NoteListEvent.OnAddNoteClicked -> {
                    _effects.send(NoteListEffect.NavigateToAddNote(null))
                }

                is NoteListEvent.OnNoteClicked -> {
                    _effects.send(NoteListEffect.NavigateToAddNote(event.noteId))
                }
            }
        }
    }
}

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface NoteListEvent {
    data object OnAddNoteClicked : NoteListEvent
    data class OnNoteClicked(val noteId: Int) : NoteListEvent
}

sealed interface NoteListEffect {
    data class NavigateToAddNote(val noteId: Int?) : NoteListEffect
}