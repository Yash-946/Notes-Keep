package com.example.noteskeep.Database

import androidx.lifecycle.LiveData
import com.example.noteskeep.Models.Note

class NoteRepository(private val noteDao:NoteDao) {

    val allNotes : LiveData<List<Note>> = noteDao.getallnotes()

    suspend fun insert(note:Note){
        noteDao.insert(note)
    }

    suspend fun delete(note:Note){
        noteDao.delete(note)
    }

    suspend fun update(note:Note){
        noteDao.update(note.id, note.title,note.note)
    }

}