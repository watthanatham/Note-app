package com.watthanatham.easynotes

import android.app.Application
import com.watthanatham.easynotes.data.NotesRoomDatabase

class NoteApplication: Application() {
    val database: NotesRoomDatabase by lazy { NotesRoomDatabase.getDatabase(this) }
}