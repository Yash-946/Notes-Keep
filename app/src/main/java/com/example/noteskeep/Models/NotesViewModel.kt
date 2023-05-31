package com.example.noteskeep.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.appendPlaceholders
import com.example.noteskeep.Database.NoteDatabase
import com.example.noteskeep.Database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : NoteRepository

    val allnote:LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        allnote = repository.allNotes
    }

    fun deleteNote(note:Note) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }

    fun insertNote(note:Note) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun updateNote(note:Note) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }


}