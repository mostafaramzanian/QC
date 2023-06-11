package com.project.test.utils

import java.text.SimpleDateFormat
import java.util.Calendar

class CurrentTime {

    fun time(): String {
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return formatter.format(currentTime)
    }


}