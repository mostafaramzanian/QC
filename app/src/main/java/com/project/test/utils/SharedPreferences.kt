package com.project.test.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", AppCompatActivity.MODE_PRIVATE)
    private val editor= sharedPreferences.edit()

    fun putInt(key:String,value:Int){
        editor.putInt(key, value)
        editor.apply()
    }
    fun putString(key:String,value:String){
        editor.putString(key, value)
        editor.apply()
    }
    fun putBoolean(key:String,value:Boolean){
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getInt(key:String,defValue:Int):Int {
        val value= sharedPreferences.getInt(key, defValue)
        return (value)
    }

    fun getString(key:String,defValue:String):String {
       val value= sharedPreferences.getString(key, defValue)
        return (value.toString())
    }

    fun getBoolean(key:String,defValue:Boolean): Boolean {
        val value= sharedPreferences.getBoolean(key, defValue)
        return (value)
    }

    fun clear(){
        editor.clear()
        editor.apply()
    }
    fun remove(key:String){
        editor.remove(key)
        editor.apply()
    }
}