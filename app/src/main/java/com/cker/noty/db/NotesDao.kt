package com.cker.noty.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cker.noty.data.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Query("SELECT * FROM notes WHERE id IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg note: Note)

    @Delete
    fun delete(note: Note)
}
