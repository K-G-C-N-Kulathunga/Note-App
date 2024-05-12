package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabasHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "note"
        private const val COLUMNS_ID = "id"
        private const val COLUMNS_TITLE = "title"
        private const val COLUMNS_CONTENT = "content"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMNS_ID INTEGER PRIMARY KEY, $COLUMNS_TITLE TEXT, $COLUMNS_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note){
        val db = writableDatabase
        val value = ContentValues().apply{
            put(COLUMNS_TITLE, note.title)
            put(COLUMNS_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null ,value)
        db.close()
    }

    fun getAllNotes():List<Note>{
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null )

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_CONTENT))

            val note = Note(id, title, content)
            notesList.add(note)

        }
        cursor.close()
        db.close()
        return notesList
    }
    fun updateNote(note: Note){
        val db=writableDatabase
        val values=ContentValues().apply {
            put(COLUMNS_TITLE,note.title)
            put(COLUMNS_CONTENT,note.content)
        }
        val whereClause = "$COLUMNS_ID= ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getNoteByID(noteId:Int):Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_ID=$noteId"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)

    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMNS_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}