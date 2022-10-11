package com.watthanatham.easynotes

import android.util.Log
import androidx.lifecycle.*
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NoteViewModel(private val noteDao: NoteDao): ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()
    val normalNote: LiveData<List<Note>> = noteDao.getNoteByStatusNormal().asLiveData()
    val mediumNote: LiveData<List<Note>> = noteDao.getNoteByStatusMedium().asLiveData()
    val highNote: LiveData<List<Note>> = noteDao.getNoteByStatusHigh().asLiveData()

    private fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }
    private fun getNewNoteEntry(titleName: String, priority: Int, description: String, dateTime: String): Note {
        return Note(
            titleName = titleName,
            priority = priority,
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
            priority = priority,
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
    fun isEntryValid(titleName: String, description: String) : Boolean {
        if(titleName.isBlank() || description.isBlank()) {
            return false
        }
        return true
    }
//    fun getNoteUsingName(titleName: String) {
//        viewModelScope.launch {
//            noteDao.getNoteByName(titleName)
//        }
//    }

}
class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}