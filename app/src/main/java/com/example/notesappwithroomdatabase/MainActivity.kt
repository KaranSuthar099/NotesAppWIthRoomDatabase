package com.example.notesappwithroomdatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappwithroomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseDao: NotesDao
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO have to implement that scroll to refresh functionality
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        databaseDao = NotesDatabase.getDatabase(this).notesDao()

        adapter = NotesAdapter(listOf<Note>(), databaseDao, lifecycleScope, this)
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter


        binding.fabCreateNote.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshNotesAdapter()
    }

    fun refreshNotesAdapter() {
        lifecycleScope.launch(Dispatchers.IO) {
            val notesList = databaseDao.selectAll()

            withContext(Dispatchers.Main) {
                if (notesList.isNotEmpty()) adapter.updateList(notesList)
            }
        }
        Toast.makeText(this, "Data refreshed in adapter", Toast.LENGTH_SHORT).show()
    }
}