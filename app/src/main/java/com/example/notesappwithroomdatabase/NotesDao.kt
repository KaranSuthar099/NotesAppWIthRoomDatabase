package com.example.notesappwithroomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {

    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note:Note)

    @Query("SELECT * From Notes")
    fun selectAll(): List<Note>

}