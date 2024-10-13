package com.cker.noty.domain.repository

import com.cker.noty.data.model.Note

interface NotesRepository {
    suspend fun getNotes(): List<Note>
    suspend fun getNoteById(noteId: Int): Note?
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNoteById(noteId: Int)
}