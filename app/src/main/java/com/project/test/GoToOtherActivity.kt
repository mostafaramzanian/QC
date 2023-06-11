package com.project.test

import android.app.Activity
import android.content.Context
import android.content.Intent

class GoToOtherActivity(private val context: Activity) {
    fun mainActivity(){
         val intent = Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent);
        context.finish()
    }
    fun login(){
        val intent = Intent(context, Login::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent);
        context.finish()
    }
}