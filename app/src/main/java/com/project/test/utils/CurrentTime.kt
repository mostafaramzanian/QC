package com.project.test.utils

import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CurrentTime {

    fun time(): String {
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return formatter.format(currentTime)
    }

    fun date(data:String): Triple<String,String,String>{
        val dateFormat = SimpleDateFormat("y-M-d H:m:s.S", Locale.US)
        val date = dateFormat.parse(data)
        val timeString = SimpleDateFormat("HH:mm:ss", Locale.US).format(date!!)
        val persianDate = PersianDate(date.time)

        val finalDate = "%s %s %02d / %02d / %d".format(
            timeString,
            persianDate.dayName(),
            persianDate.shDay.takeIf { it >= 10 } ?: persianDate.shDay,
            persianDate.shMonth.takeIf { it >= 10 } ?: persianDate.shMonth,
            persianDate.shYear)

        val finalDate1 = "%s %02d / %02d / %d".format(
            persianDate.dayName(),
            persianDate.shDay.takeIf { it >= 10 } ?: persianDate.shDay,
            persianDate.shMonth.takeIf { it >= 10 } ?: persianDate.shMonth,
            persianDate.shYear)

        return Triple(finalDate,timeString,finalDate1)
    }

}