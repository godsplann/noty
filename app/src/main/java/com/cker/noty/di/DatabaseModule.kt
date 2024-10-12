package com.cker.noty.di

import android.content.Context
import androidx.room.Insert
import androidx.room.Room
import com.cker.noty.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providesDb(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "noty-db")
            .build()

}