package com.tardigrade.capstonebangkit.utils

import android.util.Log
import java.util.*

fun isSameDay(time1: Date, time2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply {
        time = time1
    }
    val cal2 = Calendar.getInstance().apply {
        time = time2
    }

    return cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
            cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
}

fun getMidnight(time: Date, add: Int = 0) : Long {
    val calendar = Calendar.getInstance().apply {
        this.time = time
        add(Calendar.DATE, 1)
    }
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0

    return calendar.timeInMillis
}