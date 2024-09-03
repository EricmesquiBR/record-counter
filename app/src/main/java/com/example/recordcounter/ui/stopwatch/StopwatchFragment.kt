package com.example.recordcounter.ui.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.recordcounter.R
import com.example.recordcounter.databinding.FragmentStopwatchBinding
import com.example.recordcounter.utils.TimerService
import kotlin.math.roundToInt

class StopwatchFragment : Fragment() {

    private lateinit var binding: FragmentStopwatchBinding
    private lateinit var viewModel: StopwatchViewModel
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(StopwatchViewModel::class.java)

        serviceIntent = Intent(activity?.applicationContext, TimerService::class.java)
        activity?.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        binding.btnStartStop.setOnClickListener {
            startStop()
        }

        binding.btnSaveTime.setOnClickListener {
            saveTime()
        }

        return binding.root
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, minute: Int, second: Int): String =
        String.format("%02d:%02d:%02d", hour, minute, second)

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
            binding.timer.text = getTimeStringFromDouble(time)
        }
    }

    private fun startStop() {
        if (!timeStarted)
            startTimer()
        else
            stopTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time)
        activity?.startService(serviceIntent)
        binding.btnStartStop.text = "Stop"
        timeStarted = true
    }

    private fun stopTimer() {
        activity?.stopService(serviceIntent)
        binding.btnStartStop.text = "Start"
        timeStarted = false
    }

    private fun saveTime() {
        // Temporary alert for saved time
        val timeString = getTimeStringFromDouble(time)
        binding.root.let {
            android.widget.Toast.makeText(it.context, "Saved time is $timeString", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(updateTime)
    }
}


