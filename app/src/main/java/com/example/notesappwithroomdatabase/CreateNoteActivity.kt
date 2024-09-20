package com.example.notesappwithroomdatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesappwithroomdatabase.databinding.ActivityCreateNoteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var databaseDao: NotesDao
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseDao = NotesDatabase.getDatabase(this).notesDao()

        if(intent.hasExtra("NOTE")) {
            note = intent.getSerializableExtra("NOTE", Note::class.java)!!

            binding.titleInputEditText.setText(note!!.title)
            binding.contentInputEditText.setText(note!!.title)
        }

        binding.toolbar.setNavigationOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(this)
            alertDialog.setTitle("Exit ?")
            alertDialog.setMessage("Are You Sure you want to exit?\n the current data will not be saved")
            alertDialog.setPositiveButton("Yes") { dialogInterface, i -> finish() }
            alertDialog.setNeutralButton("No") { _, _ -> }
            alertDialog.show()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.saveIcon -> {
                    val title = binding.titleInputEditText.text.toString()
                    val content = binding.contentInputEditText.text.toString()

                    if(title.isEmpty()){
                        binding.titleInputLayout.error = "Title Can't be empty"
                        return@setOnMenuItemClickListener true
                    }

                    if(content.isEmpty()){
                        binding.contentInputLayout.error = "Content Can't be empty"
                        return@setOnMenuItemClickListener true
                    }


                    lifecycleScope.launch(Dispatchers.IO) {
                        if(note != null){
                            note!!.title = title
                            note!!.content = content
                            databaseDao.update(note!!)
                        } else {
                            databaseDao.insert(Note(null, title, content))

                        }
                        finish()
                    }
                }
            }
            true
        }
    }
}