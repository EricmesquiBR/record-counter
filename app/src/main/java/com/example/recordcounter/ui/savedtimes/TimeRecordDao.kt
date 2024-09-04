package com.example.recordcounter.ui.savedtimes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimeRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timeRecord: TimeRecord)

    @Query("SELECT * FROM time_records")
    fun getAllRecords(): LiveData<List<TimeRecord>>

    @Delete
    suspend fun delete(timeRecord: TimeRecord)
}
