package com.example.recordcounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recordcounter.utils.TimerService
import kotlin.math.roundToInt

class NameTimeActivity : AppCompatActivity() {

    private lateinit var tvTime: TextView
    private lateinit var etName: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_time)

        tvTime = findViewById(R.id.tvTime)
        etName = findViewById(R.id.etName)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

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
            val resultIntent = Intent().apply {
                putExtra("RECORD_NAME", name)
                putExtra("RECORD_TIME", tvTime.text.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            // Handle empty name input
            etName.error = "Please enter a name"
        }
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
