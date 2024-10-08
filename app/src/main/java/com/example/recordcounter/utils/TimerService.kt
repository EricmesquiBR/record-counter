package com.example.recordcounter.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask

class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null
    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) : Int {
        //super.onStart(intent, startId)
        val time = intent.getDoubleExtra(TIMER_EXTRA, 0.0)
        timer.scheduleAtFixedRate( TimeTask(time), 0, 1000 )
        return START_NOT_STICKY
    }
    private inner class TimeTask(private var time:Double) : TimerTask(){
        override fun run() {
            val intent = Intent(TIMER_UPDATED)
            time++ // time = time + 1
            intent.putExtra(TIMER_EXTRA, time)
            sendBroadcast(intent)
        }
    }
    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
    companion object{
        const val TIMER_UPDATED = "timerUpdate"
        const val TIMER_EXTRA = "timerUpdate"
    }
}