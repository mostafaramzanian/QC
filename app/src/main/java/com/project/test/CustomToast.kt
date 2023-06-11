package com.project.test

import android.content.Context
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class CustomToast(private val context: Context) {

     fun toastValid(spannableString: SpannableString?,text:String?){
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast,null)
         layout.findViewById<ConstraintLayout>(R.id.toastValid).visibility=View.VISIBLE
        val textView = layout.findViewById<TextView>(R.id.textToastValid)
        if (spannableString != null) {
            textView.text = spannableString
        } else if (text != null) {
            textView.text = text
        }
        val toast = Toast(context)
        toast.view = layout
        toast.show()
    }
    fun toastAlert(spannableString: SpannableString?,text:String?){
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast,null)
        layout.findViewById<ConstraintLayout>(R.id.toastAlert).visibility=View.VISIBLE
        val textView = layout.findViewById<TextView>(R.id.textToastAlert)
        if (spannableString != null) {
            textView.text = spannableString
        } else if (text != null) {
            textView.text = text
        }
        val toast = Toast(context)
        toast.view = layout
        toast.show()
    }
}