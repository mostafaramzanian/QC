package com.project.test.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AutoStart : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
          //  val intent1 = Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          //  context.startActivity(intent1);
        val rememberMe = SharedPreferences(context).getBoolean("rememberMe", false)
        if (rememberMe) {
            context.startService(Intent(context, MyService::class.java))
        }

    }
}