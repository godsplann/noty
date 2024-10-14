package com.cker.noty.data

import com.cker.noty.data.model.Note
import com.cker.noty.db.NotesDao
import com.cker.noty.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override fun getNotes(): Flow<List<Note>> {
        return notesDao.getAll()
    }

    override suspend fun getNoteById(noteId: Int): Note? {
        return notesDao.getNoteById(noteId)
    }

    override suspend fun addNote(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun deleteNoteById(noteId: Int) {
        notesDao.delete(noteId)
    }

}