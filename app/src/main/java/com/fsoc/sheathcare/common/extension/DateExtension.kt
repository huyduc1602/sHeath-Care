package com.fsoc.sheathcare.common.extension

import java.util.*

fun today(): Date {
    return Calendar.getInstance().time
}

fun yesterday(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)

    return calendar.time
}

fun Calendar.beginDay() = Calendar.getInstance().also {
    it.timeInMillis = this.timeInMillis
    it[Calendar.HOUR_OF_DAY] = 0
    it[Calendar.MINUTE] = 0
    it[Calendar.SECOND] = 0
    it[Calendar.MILLISECOND] = 0
}.time