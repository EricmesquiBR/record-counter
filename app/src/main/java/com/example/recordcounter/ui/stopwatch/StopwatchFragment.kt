@file:Suppress("DEPRECATION")

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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import kotlin.math.abs

@Suppress("DEPRECATION")
class StopwatchFragment : Fragment() {

    private lateinit var binding: FragmentStopwatchBinding
    private lateinit var viewModel: StopwatchViewModel
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    // Sensor variables
    private lateinit var sensorManager: SensorManager
    private val accelerometerListener = AccelerometerListener()
    private var lastAcceleration = FloatArray(3)
    private var lastAccelerationTime = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(StopwatchViewModel::class.java)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

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

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerListener)
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
        if (!timeStarted) {
            startTimer()
        } else {
            stopTimer()
        }
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
        val timeString = getTimeStringFromDouble(time)
        Toast.makeText(context, "Time to be saved: $timeString", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "Saved time: $timeString with name: $name", Toast.LENGTH_SHORT).show()
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

    inner class AccelerometerListener : SensorEventListener {
        private var lastActionTime = System.currentTimeMillis()
        private val COOLDOWN_PERIOD = 2000L // 2 seconds cooldown period

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            if (lastAcceleration.isNotEmpty()) {
                val deltaX = abs(x - lastAcceleration[0])
                val deltaY = abs(y - lastAcceleration[1])
                val deltaZ = abs(z - lastAcceleration[2])

                // Detect large movements
                if ((deltaX > LARGE_MOVEMENT_THRESHOLD || deltaY > LARGE_MOVEMENT_THRESHOLD || deltaZ > LARGE_MOVEMENT_THRESHOLD)
                    && System.currentTimeMillis() - lastActionTime > COOLDOWN_PERIOD) {
                    performLargeMovementAction()
                    lastActionTime = System.currentTimeMillis()
                }

                // Detect taps
                if ((deltaX > TAP_THRESHOLD || deltaY > TAP_THRESHOLD || deltaZ > TAP_THRESHOLD)
                    && System.currentTimeMillis() - lastActionTime > COOLDOWN_PERIOD) {
                    performTapAction()
                    lastActionTime = System.currentTimeMillis()
                }
            }

            lastAcceleration = event.values.clone()
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        private fun performLargeMovementAction() {
            Log.d("AccelerometerListener", "Large movement detected")
            if (!timeStarted) {
                startTimer()
            }
        }

        private fun performTapAction() {
            Log.d("AccelerometerListener", "Tap detected")
            // No action needed for tap; keeping this function for any future implementation
        }
    }

    companion object {
        private const val REQUEST_CODE_SAVE_TIME = 1
        private const val LARGE_MOVEMENT_THRESHOLD = 8f
        private const val TAP_THRESHOLD = 2f
        private const val COOLDOWN_PERIOD = 2000L // 2 seconds cooldown period
    }
}
