package com.project.test.utils

import android.app.Activity
import android.content.Intent
import com.project.test.view.activity.LoginActivity
import com.project.test.view.activity.MainActivity

class GoToOtherActivity(private val context: Activity) {
    fun mainActivity(){
         val intent = Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent);
        context.finish()
    }
    fun login(){
        val intent = Intent(context, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent);
        context.finish()
    }
}