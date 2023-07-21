package com.project.test.utils

import android.content.Context
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.test.R

class CustomToast(private val context: Context) {
    companion object {
        private val toasts = mutableListOf<Toast>()
    }

    fun toastValid(
        spannableString: SpannableString?,
        text: String?,
        size: Float?,
        position: Int?
    ) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast, null)
        layout.findViewById<ConstraintLayout>(R.id.toastValid).visibility = View.VISIBLE
        val textView = layout.findViewById<TextView>(R.id.textToastValid)
        if (spannableString != null) {
            textView.text = spannableString.toString()
        } else if (text != null) {
            textView.text = text

            if (position != null) {
                textView.gravity = position
            }

            if (size != null) {
                textView.textSize = size
            }
        }
        val toast = Toast(context)
        toast.view = layout
        toast.duration = LENGTH_LONG
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.show()
        toasts.add(toast)
    }

    fun toastAlert(spannableString: SpannableString?, text: String?, size: Float?, position: Int?) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast, null)
        layout.findViewById<ConstraintLayout>(R.id.toastAlert).visibility = View.VISIBLE
        val textView = layout.findViewById<TextView>(R.id.textToastAlert)
        if (spannableString != null) {
            textView.text = spannableString
        } else if (text != null) {
            textView.text = text
            if (position != null) {
                textView.gravity = position
            }

            if (size != null) {
                textView.textSize = size
            }
        }
        val toast = Toast(context)
        toast.view = layout
        toast.duration = LENGTH_LONG
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.show()
        toasts.add(toast)
    }

    fun cancelAllToasts(number: Int) {
        val size = toasts.size
        for (i in 0 until size - number) {
            toasts[i].cancel()
        }
    }
}