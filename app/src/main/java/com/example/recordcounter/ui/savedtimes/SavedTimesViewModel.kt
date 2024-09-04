// SavedTimesViewModel.kt
package com.example.recordcounter.ui.savedtimes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavedTimesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TimeRecordRepository

    init {
        val timeRecordDao = RecordCounterDatabase.getDatabase(application).timeRecordDao()
        repository = TimeRecordRepository(timeRecordDao)
    }

    fun getAllRecords(): LiveData<List<TimeRecord>> {
        return repository.allRecords
    }

    suspend fun insert(timeRecord: TimeRecord) {
        withContext(Dispatchers.IO) {
            repository.insert(timeRecord)
        }
    }

    suspend fun delete(timeRecord: TimeRecord) {
        withContext(Dispatchers.IO) {
            repository.delete(timeRecord)
        }
    }
}
