package com.cker.noty.ui.addorupdatenote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cker.noty.data.model.Note
import com.cker.noty.domain.usecase.NoteCrudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateNoteViewModel @Inject constructor(
    private val noteCrudUseCase: NoteCrudUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(AddOrUpdateNoteState())
    val state: StateFlow<AddOrUpdateNoteState> = _state

    private val _effects = MutableSharedFlow<AddOrUpdateNoteEffect>()
    val effects = _effects.asSharedFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            _state.value = _state.value.copy(isEditing = true)
            getNoteToBeEdited(noteId)
        } ?: run {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun getNoteToBeEdited(noteId: Int) {
        viewModelScope.launch {
            noteCrudUseCase.getNoteById(noteId)?.let {
                _state.value = _state.value.copy(
                    note = it,
                    isLoading = false
                )
            } ?: _effects.emit(AddOrUpdateNoteEffect.ShowError("Note not found"))
        }
    }

    fun onEvent(event: AddOrUpdateNoteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is AddOrUpdateNoteEvent.OnNoteSaved -> {
                    when {
                        _state.value.note.title.isEmpty() -> {
                            _effects.emit(AddOrUpdateNoteEffect.ShowError("Title cannot be empty"))
                        }

                        _state.value.note.content.isEmpty() -> {
                            _effects.emit(AddOrUpdateNoteEffect.ShowError("Description cannot be empty"))
                        }

                        else -> {
                            if (_state.value.isEditing) {
                                updateNote()
                            } else {
                                addNote()
                            }
                            onEvent(AddOrUpdateNoteEvent.OnBackPressed)
                        }
                    }
                }

                is AddOrUpdateNoteEvent.OnNoteSaveFailed -> {
                    _effects.emit(AddOrUpdateNoteEffect.ShowError(event.error))
                }

                is AddOrUpdateNoteEvent.OnTitleChanged -> {
                    _state.value =
                        _state.value.copy(note = _state.value.note.copy(title = event.title))
                }

                is AddOrUpdateNoteEvent.OnContentChanged -> {
                    _state.value =
                        _state.value.copy(note = _state.value.note.copy(content = event.content))
                }

                AddOrUpdateNoteEvent.OnBackPressed -> {
                    _effects.emit(AddOrUpdateNoteEffect.NavigateBack)
                }
            }
        }
    }

    private suspend fun addNote() {
        noteCrudUseCase.addNote(
            Note(
                title = _state.value.note.title,
                content = _state.value.note.content,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    private suspend fun updateNote() {
        noteCrudUseCase.updateNote(
            _state.value.note.copy(
                title = _state.value.note.title,
                content = _state.value.note.content,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
