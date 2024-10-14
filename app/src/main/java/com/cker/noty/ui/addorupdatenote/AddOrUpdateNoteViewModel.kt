package com.cker.noty.ui.addorupdatenote

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cker.noty.data.model.Note
import com.cker.noty.domain.usecase.NoteCrudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateNoteViewModel @Inject constructor(
    private val noteCrudUseCase: NoteCrudUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(AddOrUpdateNoteState())
    val state: StateFlow<AddOrUpdateNoteState> = _state

    private val _effects = Channel<AddOrUpdateNoteEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        val noteId = savedStateHandle.get<Int>("noteId")
        if (noteId != null) {
            _state.value = _state.value.copy(isEditing = true)
            getNoteToBeEdited(noteId)
        }
    }

    private fun getNoteToBeEdited(noteId: Int) {
        viewModelScope.launch {
            noteCrudUseCase.getNoteById(noteId)?.let {
                _state.value = _state.value.copy(
                    note = it
                )
            } ?: _effects.send(AddOrUpdateNoteEffect.ShowError("Note not found"))

        }
    }

    fun onEvent(event: AddOrUpdateNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddOrUpdateNoteEvent.OnNoteSaved -> {
                    when {
                        _state.value.note.title.isEmpty() -> {
                            _effects.send(AddOrUpdateNoteEffect.ShowError("Title cannot be empty"))
                        }

                        _state.value.note.content.isEmpty() -> {
                            _effects.send(AddOrUpdateNoteEffect.ShowError("Description cannot be empty"))
                        }

                        else -> {
                            withContext(Dispatchers.IO) {
                                if (_state.value.isEditing) {
                                    noteCrudUseCase.updateNote(
                                        _state.value.note.copy(
                                            title = _state.value.note.title,
                                            content = _state.value.note.content,
                                            updatedAt = System.currentTimeMillis()
                                        )
                                    )
                                } else {
                                    noteCrudUseCase.addNote(
                                        Note(
                                            title = _state.value.note.title,
                                            content = _state.value.note.content,
                                            createdAt = System.currentTimeMillis(),
                                            updatedAt = System.currentTimeMillis()
                                        )
                                    )
                                }
                            }
                        }
                    }

                }

                is AddOrUpdateNoteEvent.OnNoteSaveFailed -> {
                    _effects.send(AddOrUpdateNoteEffect.ShowError(event.error))
                }

                is AddOrUpdateNoteEvent.OnTitleChanged -> {
                    _state.value =
                        _state.value.copy(note = _state.value.note.copy(title = event.title))
                }

                is AddOrUpdateNoteEvent.OnContentChanged -> {
                    _state.value =
                        _state.value.copy(note = _state.value.note.copy(content = event.content))
                }
            }
        }
    }
}

@Immutable
data class AddOrUpdateNoteState(
    val note: Note = Note(
        title = "",
        content = "",
        createdAt = 0,
        updatedAt = 0
    ),
    val isEditing: Boolean = false
)

sealed interface AddOrUpdateNoteEvent {
    data class OnTitleChanged(val title: String) : AddOrUpdateNoteEvent
    data class OnContentChanged(val content: String) : AddOrUpdateNoteEvent
    data object OnNoteSaved : AddOrUpdateNoteEvent
    data class OnNoteSaveFailed(val error: String) : AddOrUpdateNoteEvent
}

sealed interface AddOrUpdateNoteEffect {
    data object NavigateToNoteList : AddOrUpdateNoteEffect
    data class ShowError(val error: String) : AddOrUpdateNoteEffect
}