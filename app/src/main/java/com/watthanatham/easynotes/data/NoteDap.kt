package com.watthanatham.easynotes.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(notes: Note)

    @Delete
    suspend fun delete(notes: Note)

    @Query("SELECT * from note WHERE id = :id")
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * from note WHERE title = :title")
    fun getNoteByName(title: String): Flow<Note>

    @Query("SELECT * from note ORDER BY title ASC")
    fun getNotes(): Flow<List<Note>>
}