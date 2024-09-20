package com.example.notesappwithroomdatabase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesAdapter(
    var notesList: List<Note>,
    var databaseDao: NotesDao,
    var lifecycleScope: LifecycleCoroutineScope,
    var context: MainActivity
): RecyclerView.Adapter<NotesAdapter.viewHolder>() {

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val content = itemView.findViewById<TextView>(R.id.tvContent)
        val delete = itemView.findViewById<Button>(R.id.btnDelete)
        val card = itemView.findViewById<CardView>(R.id.NoteItemCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_view, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val note = notesList[position]
        holder.title.setText(note.title)
        holder.content.setText(note.content)

        holder.delete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                databaseDao.delete(note)

                // Fetch updated list and update UI on main thread
                val updatedNotes = databaseDao.selectAll()
                withContext(Dispatchers.Main) {
                    updateList(updatedNotes)
                }
            }
        }

        holder.card.setOnClickListener {
            var intent = Intent(context, CreateNoteActivity::class.java).putExtra("NOTE", note)
            context.startActivity(intent)
        }
    }

    fun updateList(notesList: List<Note>){
        this.notesList = notesList
        notifyDataSetChanged()
    }
}