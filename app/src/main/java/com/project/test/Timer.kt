package com.project.test

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask

class Timer(private val context: Context) {
   private val handler = Handler()
    private val timer = Timer()
    fun startTimer() {
        /*
       handler.postDelayed({
           Toast.makeText(context, "Your toast message", Toast.LENGTH_SHORT).show()
       }, 10000)
          */

       val timerTask = object : TimerTask() {
           override fun run() {
               (context as Activity).runOnUiThread {
                   Toast.makeText(context, "Your toast message", Toast.LENGTH_SHORT).show()
               }
           }
       }

       timer.schedule(timerTask, 0, 1000)


    }

    fun stopTimer() {
       // handler.removeCallbacksAndMessages(null)
        timer.cancel()
    }
}

