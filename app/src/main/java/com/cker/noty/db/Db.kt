package com.cker.noty.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cker.noty.data.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}