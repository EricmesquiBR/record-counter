// NameTimeActivity.kt
package com.example.recordcounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recordcounter.ui.savedtimes.SavedTimesViewModel
import com.example.recordcounter.ui.savedtimes.TimeRecord
import com.example.recordcounter.utils.TimerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

class NameTimeActivity : AppCompatActivity() {

    private lateinit var tvTime: TextView
    private lateinit var etName: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var viewModel: SavedTimesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_time)

        tvTime = findViewById(R.id.tvTime)
        etName = findViewById(R.id.etName)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Initialize ViewModel here
        viewModel = ViewModelProvider(this)[SavedTimesViewModel::class.java]

        // Get the time from the intent
        val time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
        tvTime.text = getTimeStringFromDouble(time)

        btnSave.setOnClickListener {
            saveRecord()
        }

        btnCancel.setOnClickListener {
            cancel()
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun saveRecord() {
        val name = etName.text.toString()
        if (name.isNotBlank()) {
            val timeString = tvTime.text.toString()

            // Use runBlocking to ensure the operation completes before proceeding
            GlobalScope.launch(Dispatchers.IO) {
                runBlocking {
                    viewModel.insert(TimeRecord(name = name, time = timeString))
                }

                // Move back to main thread after insertion
                launch(Dispatchers.Main) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        } else {
            etName.error = "Please enter a name"
        }
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
