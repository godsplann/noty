package com.cker.noty.di

import android.content.Context
import androidx.room.Room
import com.cker.noty.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providesNotesDao(db: AppDatabase) = db.notesDao()

    @Provides
    fun providesDb(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "noty-db")
            .build()

}