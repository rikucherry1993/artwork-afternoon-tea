package com.rikucherry.artworkespresso.common.tool

import androidx.core.text.isDigitsOnly
import java.text.SimpleDateFormat
import java.util.*

object DataFormatHelper {
    val weeklyDates = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    fun convertLongStringToDate(time: String?): String? {
        if (time == null || time.isEmpty() || !time.isDigitsOnly()) {
            return null
        }
        val timeLong = time.toLong() * 1000
        return convertTimeStampToDate(timeLong)
    }

    fun convertTimeStampToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.US)
        return format.format(date)
    }

    fun convertTimeStampToWeekday(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("EEE", Locale.US)
        return format.format(date)
    }

    fun getWeekdayOfToday(): String {
        val date = Date()
        val format = SimpleDateFormat("EEE", Locale.US)
        return format.format(date)
    }

    fun getWeekDayOfTimeStamp(time: String?): String {
        if (time == null || time.isEmpty() || !time.isDigitsOnly()) {
            return "Invalid"
        }

        val timeLong = time.toLong() * 1000
        return convertTimeStampToWeekday(timeLong)
    }

    fun getDateFromWeekday(weekday: String): String {
        val index = weeklyDates.indexOf(weekday)
        val indexOfToday = weeklyDates.indexOf(getWeekdayOfToday())
        val duration = index - indexOfToday

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, duration)

        return format.format(c.time)
    }

    fun getFormalDateFromWeekday(weekday: String): String {
        val index = weeklyDates.indexOf(weekday)
        val indexOfToday = weeklyDates.indexOf(getWeekdayOfToday())
        val duration = index - indexOfToday

        val format = SimpleDateFormat("EEEE, MMM d", Locale.US)
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, duration)

        return format.format(c.time)
    }


}