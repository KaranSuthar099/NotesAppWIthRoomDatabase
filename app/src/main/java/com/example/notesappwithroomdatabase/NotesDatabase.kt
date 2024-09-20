package com.example.notesappwithroomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, NotesDatabase::class.java, "notes_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}