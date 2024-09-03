package com.example.recordcounter.ui.stopwatch

import android.app.Activity
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
import com.example.recordcounter.NameTimeActivity
import com.example.recordcounter.R
import com.example.recordcounter.databinding.FragmentStopwatchBinding
import com.example.recordcounter.utils.TimerService
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
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

        binding.btnReset.setOnClickListener {
            resetTime()
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
        var timeString = getTimeStringFromDouble(time)
        binding.root.let {
            android.widget.Toast.makeText(it.context, "Saved time is $timeString", android.widget.Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(activity, NameTimeActivity::class.java).apply {
            putExtra(TimerService.TIMER_EXTRA, time)
        }
        startActivityForResult(intent, REQUEST_CODE_SAVE_TIME)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SAVE_TIME && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra("RECORD_NAME")
            val timeString = data?.getStringExtra("RECORD_TIME")
            android.widget.Toast.makeText(context, "Saved time: $timeString with name: $name", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime() {
        stopTimer()
        time = 0.0
        binding.timer.text = getTimeStringFromDouble(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(updateTime)
    }

    companion object {
        private const val REQUEST_CODE_SAVE_TIME = 1
    }

}


