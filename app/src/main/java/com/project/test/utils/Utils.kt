package com.project.test.utils

import java.math.RoundingMode
import java.text.DecimalFormat

class Utils {
    companion object {
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.####")
            df.roundingMode = RoundingMode.FLOOR
            return df.format(number).toDouble()
        }
    }

}