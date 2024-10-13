package com.cker.noty.ui.addorupdatenote

import androidx.lifecycle.SavedStateHandle
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
                    title = it.title,
                    description = it.content
                )
            } ?: _effects.send(AddOrUpdateNoteEffect.ShowError("Note not found"))

        }
    }

    fun onEvent(event: AddOrUpdateNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddOrUpdateNoteEvent.OnNoteSaved -> {
                    noteCrudUseCase.addNote(
                        Note(
                            title = _state.value.title,
                            content = _state.value.description,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                    _effects.send(AddOrUpdateNoteEffect.NavigateToNoteList)
                }

                is AddOrUpdateNoteEvent.OnNoteSaveFailed -> {
                    _effects.send(AddOrUpdateNoteEffect.ShowError(event.error))
                }

                is AddOrUpdateNoteEvent.OnTitleChanged -> {
                    _state.value = _state.value.copy(title = event.title)
                }

                is AddOrUpdateNoteEvent.OnDescriptionChanged -> {
                    _state.value = _state.value.copy(description = event.description)
                }
            }
        }
    }
}

data class AddOrUpdateNoteState(
    val title: String = "",
    val description: String = "",
    val isEditing: Boolean = false
)

sealed interface AddOrUpdateNoteEvent {
    data class OnTitleChanged(val title: String) : AddOrUpdateNoteEvent
    data class OnDescriptionChanged(val description: String) : AddOrUpdateNoteEvent
    data object OnNoteSaved : AddOrUpdateNoteEvent
    data class OnNoteSaveFailed(val error: String) : AddOrUpdateNoteEvent
}

sealed interface AddOrUpdateNoteEffect {
    data object NavigateToNoteList : AddOrUpdateNoteEffect
    data class ShowError(val error: String) : AddOrUpdateNoteEffect
}