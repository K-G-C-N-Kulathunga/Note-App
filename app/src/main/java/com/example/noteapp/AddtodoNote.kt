package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteapp.databinding.ActivityAddtodoNoteBinding
import com.example.noteapp.databinding.ActivityMainBinding

class AddtodoNote : AppCompatActivity() {


    private lateinit var binding: ActivityAddtodoNoteBinding
    private lateinit var db : NoteDatabasHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddtodoNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabasHelper(this)



        binding.saveButton.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            val content = binding.contentEdit.text.toString()
            val note = Note (0,title, content)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()

        }
    }
}