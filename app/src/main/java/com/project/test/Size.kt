package com.project.test

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class Size(context: Context) {
    private val resources = context.resources
    private val displayMetrics = resources.displayMetrics
    private val width = displayMetrics.widthPixels
    private val height = displayMetrics.heightPixels
    fun fontSize(fontSize: Float): Float {
        return fontSize * width
    }

    fun calWidth(width2: Float): Int {
        return (width * width2).toInt()
    }

    fun calHeight(height1: Float): Int {
        return (height * height1).toInt()
    }

    fun changeFontSize(fontSize:Float,view:View){
        if(view is TextView) {
            val fontSize1 = fontSize * width
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize1)
        }
        if(view is EditText)
        {
            val fontSize1 = fontSize * width
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize1)
        }
    }
    fun changeSize(width2: Float, height1: Float,view: View) {
        val width3 = (width * width2).toInt()
        val height3 = (height * height1).toInt()
        val layoutParams =  view.layoutParams as ViewGroup.LayoutParams
        layoutParams.width = width3
        layoutParams.height = height3
        view.layoutParams = layoutParams
    }
}