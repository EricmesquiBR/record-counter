package com.example.recordcounter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recordcounter.databinding.ActivityStopwatchBinding
import com.example.recordcounter.utils.TimerService
import kotlin.math.roundToInt

class StopwatchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStopwatchBinding
    private var timeStarted = false
    private lateinit var serviceIntent : Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cronometro)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        binding.btnStartStop.setOnClickListener {
            startStop()
        }

        binding.btnReset.setOnClickListener {
            resetTime()
        }

        binding.btnSaveTime.setOnClickListener {
            saveTime()
        }
    }

    private fun getTimeStringFromDouble( time : Double ) : String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString( hour : Int, minute : Int, second : Int) : String = String.format("%2d:%2d:%2d", hour, minute, second)

    private val updateTime : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra( TimerService.TIMER_EXTRA, 0.0 )
            binding.timer.text = getTimeStringFromDouble(time)
        }
    }

    private fun startStop(){
        if(!timeStarted)
            startTimer()
        else
            stopTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time)
        startService(serviceIntent)
        binding.btnStartStop.text = "Stop"
        timeStarted = true
    }
    private fun stopTimer() {
        stopService(serviceIntent)
        binding.btnStartStop.text = "Start"
        timeStarted = false
    }
    private fun resetTime() {
        stopTimer()
        time = 0.0
        binding.timer.text = getTimeStringFromDouble(time)
    }
    private fun saveTime() {
//        val intent = Intent(this, SaveTimeActivity::class.java)
//        intent.putExtra("SAVED_TIME", getTimeStringFromDouble(time))
//        startActivity(intent)
        val timeString = getTimeStringFromDouble(time)
        Toast.makeText(this, "Saved time is $timeString", Toast.LENGTH_SHORT).show()

    }
}