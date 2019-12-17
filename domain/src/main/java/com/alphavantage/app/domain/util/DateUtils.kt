package com.alphavantage.app.domain.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun parseDateToString(date: Date, format: String = "dd MMM yyyy"): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun parseStringToDate(dateString: String, timezoneString: String?, pattern: String = "yyyy-MM-dd HH:mm:ss"): Date {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        if (timezoneString != null)
            formatter.timeZone = TimeZone.getTimeZone(timezoneString)
        return formatter.parse(dateString)
    }
}