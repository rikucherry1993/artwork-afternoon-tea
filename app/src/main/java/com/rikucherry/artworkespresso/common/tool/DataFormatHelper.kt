package com.rikucherry.artworkespresso.common.tool

import androidx.core.text.isDigitsOnly
import java.text.SimpleDateFormat
import java.util.*

object DataFormatHelper {

    fun convertLongStringToTime(time: String?): String? {
        if (time == null || time.isEmpty() || !time.isDigitsOnly()) {
            return null
        }
        val timeLong = time.toLong() * 1000
        return convertLongToTime(timeLong)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }
}