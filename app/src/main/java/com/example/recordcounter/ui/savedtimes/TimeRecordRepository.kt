package com.example.recordcounter.ui.savedtimes

import androidx.lifecycle.LiveData

class TimeRecordRepository(private val timeRecordDao: TimeRecordDao) {

    val allRecords: LiveData<List<TimeRecord>> = timeRecordDao.getAllRecords()

    suspend fun insert(timeRecord: TimeRecord) {
        timeRecordDao.insert(timeRecord)
    }

    suspend fun delete(timeRecord: TimeRecord) {
        timeRecordDao.delete(timeRecord)
    }
}
