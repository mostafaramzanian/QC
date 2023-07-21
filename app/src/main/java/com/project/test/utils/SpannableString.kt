package com.project.test.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

class SpannableString() {
     fun spannableString(text: String?, text3: String?, color: Int,size: Float?,typeface: Typeface?): SpannableString {
        val spannableString = SpannableString(text)
        val startIndex = spannableString.indexOf(text3!!)
        val endIndex = startIndex + text3.length
        spannableString.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
         if(size!=null) {
             spannableString.setSpan(
                 RelativeSizeSpan(size),
                 startIndex,
                 endIndex,
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
             )
         }
         if(typeface!=null) {
             spannableString.setSpan(
                 StyleSpan(typeface.style),
                 startIndex,
                 endIndex,
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
             )
         }
        return spannableString
    }


}