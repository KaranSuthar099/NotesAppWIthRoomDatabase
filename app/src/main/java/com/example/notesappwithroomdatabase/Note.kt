package com.example.notesappwithroomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Notes")
class Note (

    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    var title: String,
    var content: String,
): Serializable {}