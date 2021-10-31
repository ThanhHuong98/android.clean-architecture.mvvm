package com.nphau.android.shared.libs.timezone

import android.os.Build
import androidx.annotation.RequiresApi
import com.nphau.android.shared.common.functional.CallBack
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.schedule

object TimeUtils {

    private const val SECOND_MILLIS = 1_000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    const val PATTERN_h_mm_a =                                      // 12:08 PM
        "h:mm a"
    const val PATTERN_E_dd_MMM =                                    // “Sat, 14 Jul”
        "E, dd/MMM"
    const val PATTERN_E_MMM_dd =                                    // “Sat, Jul 14”
        "E, MMM/dd"
    const val PATTERN_HH_mm_ss_a =                                  // 14:31:06 PM
        "HH:mm:ss a"
    const val PATTERN_dd_MMM_yyyy =                                 // “14-Jul-2018”
        "dd-MMM-yyyy"
    const val PATTERN_EEEE_dd_MM =                                  // “Saturday, 14/07”
        "EEEE, dd/MM"
    const val PATTERN_EEEE_MMM_dd =                                 // “Saturday, Jul 14”
        "EEEE, MMM/dd"
    const val PATTERN_EEEE_dd_MMM =                                 // “Saturday, 14 Jul”
        "EEEE, dd/MMM"
    const val PATTERN_E_MMM_dd_yyyy =                               // “Sat, Jul 14, 2018”
        "E, MMM dd, yyyy"
    const val PATTERN_EEEE_dd_MM_YYYY =                             // “Saturday, 14/07/2018”
        "EEEE, dd/MM/yyyy"
    const val PATTERN_HH_mm_dd_mm_yyyy =                            // “14:31 14/07/2018”
        "HH:mm dd/MM/yyyy"
    const val PATTERN_dd_mm_yyyy = "dd/MM/yyyy"                     // “14/07/2018”
    const val PATTERN_ISO_INSTANT =                                 // 2011-12-03T10:15:30Z
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    @JvmStatic
    fun toDate(timestamp: Long, pattern: String = PATTERN_HH_mm_dd_mm_yyyy): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date(timestamp)).toString()
    }

    @JvmStatic
    fun toUTCDate(timestamp: Long, pattern: String = PATTERN_ISO_INSTANT): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.timeZone = timeZoneUTC()
        return dateFormat.format(Date(timestamp)).toString()
    }

    @JvmStatic
    fun timeZoneDefault(): TimeZone = TimeZone.getDefault()

    @JvmStatic
    fun timeZoneUTC(): TimeZone = TimeZone.getTimeZone("UTC")

    @JvmStatic
    fun getDate(pattern: String = PATTERN_ISO_INSTANT): String {
        return toDate(getTimeStamp(), pattern)
    }

    @JvmStatic
    fun getTimeStamp() = System.currentTimeMillis()

    @JvmStatic
    fun getDate(timestamp: Long, pattern: String = PATTERN_ISO_INSTANT): String {
        return toDate(timestamp, pattern)
    }

    @JvmStatic
    fun getDateByUTC(dateInString: String, pattern: String = PATTERN_ISO_INSTANT): Date? {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.timeZone = timeZoneUTC()
        return try {
            dateFormat.parse(dateInString)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun getDateByDefault(dateInString: String, pattern: String = PATTERN_ISO_INSTANT): Date? {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.timeZone = timeZoneDefault()
        return try {
            dateFormat.parse(dateInString)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDate.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return this.atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return this.atZone(zoneId).toInstant().toEpochMilli()
    }

    @JvmStatic
    fun now(): Date = Calendar.getInstance().time

    @JvmStatic
    fun diffDays(dateFrom: Date, dateTo: Date): Int {
        val diff: Long = dateFrom.time - dateTo.time
        return (diff / 86400000).toInt()
    }

    @JvmStatic
    fun addDays(date: Date, days: Int): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DAY_OF_YEAR, days)
        return c.time
    }

    @JvmStatic
    fun delay(timeout: Long, callBack: CallBack? = null): TimerTask {
        return Timer().schedule(timeout) {
            callBack?.invoke()
        }
    }

    fun getTimeAgo(timeUnit: Long?): String? {
        if (timeUnit == null) return null
        var time = timeUnit
        if (time < 1000000000000L) { // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0)
            return null
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                "just now"
            }
            diff < 2 * MINUTE_MILLIS -> {
                "a minute ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                "${diff / MINUTE_MILLIS} minutes ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                "an hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                "${diff / HOUR_MILLIS} hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                "yesterday"
            }
            else -> {
                "${diff / DAY_MILLIS} days ago"
            }
        }
    }
}

fun Date.diffDays(date: Date): Int {
    return TimeUtils.diffDays(this, date)
}

fun Date.addDays(days: Int): Date {
    return TimeUtils.addDays(this, days)
}