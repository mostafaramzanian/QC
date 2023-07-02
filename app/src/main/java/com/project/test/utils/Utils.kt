package com.project.test.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.project.test.R
import java.math.RoundingMode
import java.text.DecimalFormat


class Utils {
    companion object {
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.####")
            df.roundingMode = RoundingMode.FLOOR
            return df.format(number).toDouble()
        }

        var exitCalled = false
        private var toast: Toast? = null

        fun exitApp(activity: Activity) {
            if (exitCalled) {
                if (toast != null)
                    toast!!.cancel()

                activity.finish()
            } else {
                toast = makeToast(
                    activity,
                    activity.getString(R.string.alert_exit),
                    Toast.LENGTH_SHORT,
                    0
                )
                exitCalled = true
                Handler(activity.mainLooper).postDelayed({ exitCalled = false }, 2000)
            }
        }

        fun getDPI(size: Float, metrics: DisplayMetrics): Int {
            return (size * metrics.densityDpi).toInt() / DisplayMetrics.DENSITY_DEFAULT
        }

        fun dpToPx(res: Resources, dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                res.displayMetrics
            ).toInt()
        }

        fun px2dp(resources: Resources, px: Float): Int {
            return (px / resources.displayMetrics.density).toInt();
        }

        fun dp2px(resources: Resources, dp: Float): Float {
            val scale: Float = resources.displayMetrics.density
            return dp * scale + 0.5f
        }

        fun sp2px(resources: Resources, sp: Float): Float {
            val scale: Float = resources.displayMetrics.scaledDensity
            return sp * scale
        }

        fun makeToast(activity: Activity, text: String?, duration: Int, type: Int): Toast? {
            val inflater = activity.layoutInflater
            val layout: View = inflater.inflate(
                R.layout.toast_layout,
                activity.findViewById(R.id.toast_layout_root)
            )
            val tv: TextView = layout.findViewById(R.id.text)
            tv.text = text
            when (type) {
                0 -> tv.setTextColor(activity.resources.getColor(R.color.white))
                -1 -> tv.setTextColor(activity.resources.getColor(R.color.error_color))
                1 -> tv.setTextColor(activity.resources.getColor(R.color.success_color))
                else -> {}
            }
            val toast = Toast(activity.applicationContext)
            toast.setGravity(
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0,
                dpToPx(activity.resources, 100).toInt()
            )
            toast.duration = duration
            toast.setView(layout)
            toast.show()
            return toast
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}