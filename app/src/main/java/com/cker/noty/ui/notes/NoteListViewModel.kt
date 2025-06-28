package com.cker.noty.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cker.noty.domain.usecase.NoteCrudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteCrudUseCase: NoteCrudUseCase
) : ViewModel() {
    val state: StateFlow<NoteListState> =
        noteCrudUseCase.getNotes().map { NoteListState(it, false) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    private val _effects = MutableSharedFlow<NoteListEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: NoteListEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is NoteListEvent.OnAddNoteClicked -> {
                    _effects.emit(NoteListEffect.NavigateToAddNote(null))
                }

                is NoteListEvent.OnNoteClicked -> {
                    _effects.emit(NoteListEffect.NavigateToAddNote(event.noteId))
                }

                is NoteListEvent.OnDeleteNoteClicked -> {
                    noteCrudUseCase.deleteNoteById(event.noteId)
                }
            }
        }
    }
}