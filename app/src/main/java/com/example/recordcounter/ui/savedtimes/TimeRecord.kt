package com.example.recordcounter.ui.savedtimes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_records")
data class TimeRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val time: String
)
