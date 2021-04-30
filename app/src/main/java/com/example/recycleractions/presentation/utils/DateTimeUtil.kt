package com.example.recycleractions.presentation.utils

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    companion object{
        fun getTimeAgo(dateTime: String): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            var ago = ""
            try {
                val time: Long = sdf.parse(dateTime).time
                val now = System.currentTimeMillis()

                ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ago
        }
    }
}