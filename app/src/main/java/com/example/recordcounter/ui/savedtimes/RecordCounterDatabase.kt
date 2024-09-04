package com.example.recordcounter.ui.savedtimes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimeRecord::class], version = 1, exportSchema = false)
abstract class RecordCounterDatabase : RoomDatabase() {
    abstract fun timeRecordDao(): TimeRecordDao

    companion object {
        @Volatile
        private var INSTANCE: RecordCounterDatabase? = null

        fun getDatabase(context: Context): RecordCounterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordCounterDatabase::class.java,
                    "record_counter_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
