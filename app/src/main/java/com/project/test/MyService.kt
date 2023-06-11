package com.project.test

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyService() : Service() {
    private val second1: Int =60
    private val hours: Int =72
    private val second = second1 * 1000
    private val milliseconds = hours * 3600000
    private val logOutTime: Long =second .toLong()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
   /*
        val context = applicationContext
        handler = Handler()
        runnable = Runnable {
            val pref= context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE)
            if(pref!=null) {
                val editor = pref.edit()
                editor.clear()
                editor.apply()
            }
            /*
            val intent1 = Intent(this, Login::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent1)
            stopSelf()

             */
        }
        handler.postDelayed(runnable, logOutTime)

        val notification = NotificationCompat.Builder(this, "Service")
            .setContentTitle(getText(R.string.services))
            .setSmallIcon(R.drawable.alert_no_report)
            .build()

        startForeground(1, notification)


    */
         return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
    }


}