package com.example.recordcounter.ui.savedtimes

import androidx.lifecycle.LiveData

class TimeRecordRepository(private val timeRecordDao: TimeRecordDao) {

    val allRecords: LiveData<List<TimeRecord>> = timeRecordDao.getAllRecords()

    suspend fun insert(timeRecord: TimeRecord): Result<Unit> {
        return try {
            timeRecordDao.insert(timeRecord)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(timeRecord: TimeRecord): Result<Unit> {
        return try {
            timeRecordDao.delete(timeRecord)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

