package com.cker.noty.domain.usecase

import com.cker.noty.data.model.Note
import com.cker.noty.domain.repository.NotesRepository
import javax.inject.Inject

class NoteCrudUseCase @Inject constructor(private val notesRepository: NotesRepository) {

    suspend fun getNotes(): List<Note> {
        return notesRepository.getNotes()
    }

    suspend fun getNoteById(noteId: Int): Note? {
        return notesRepository.getNoteById(noteId)
    }

    suspend fun addNote(note: Note) {
        notesRepository.addNote(note)
    }

    suspend fun updateNote(note: Note) {
        notesRepository.updateNote(note)
    }

    suspend fun deleteNoteById(noteId: Int) {
        notesRepository.deleteNoteById(noteId)
    }
}