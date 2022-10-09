package com.watthanatham.easynotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val titleName: String,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date_time")
    val dateTime: String
)
