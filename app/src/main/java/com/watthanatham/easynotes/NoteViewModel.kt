package com.watthanatham.easynotes

import androidx.lifecycle.*
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.data.NoteDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.concurrent.Flow

class NoteViewModel(private val noteDao: NoteDao): ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    private fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }
    private fun getNewNoteEntry(titleName: String, priority: Int, description: String, dateTime: String): Note {
        return Note(
            titleName = titleName,
            priority = priority.toInt(),
            description = description,
            dateTime = dateTime
        )
    }
    private fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }
    private fun getUpdateNoteEntry(
        noteId: Int,
        titleName: String,
        priority: Int,
        description: String,
        dateTime: String
    ): Note {
        return Note(
            id = noteId,
            titleName = titleName,
            priority = priority.toInt(),
            description = description,
            dateTime = dateTime
        )
    }
    fun addNewNote(titleName: String, priority: Int, description: String, dateTime: String) {
        val newNote = getNewNoteEntry(titleName, priority, description, dateTime)
        insertNote(newNote)
    }
    fun updateNote(noteId: Int, titleName: String, priority: Int, description: String, dateTime: String) {
        val updatedNote = getUpdateNoteEntry(noteId, titleName, priority, description, dateTime)
        updateNote(updatedNote)
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNoteById(id).asLiveData()
    }
    fun isEntryValid(titleName: String, priority: Int, description: String) : Boolean {
        if(titleName.isBlank() || priority == null || description.isBlank()) {
            return false
        }
        return true
    }
}
class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}