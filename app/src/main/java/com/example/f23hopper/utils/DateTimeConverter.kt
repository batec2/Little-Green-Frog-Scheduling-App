package com.example.f23hopper.utils

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import java.sql.Date
import java.time.DayOfWeek

class DateTypeConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }


}

fun LocalDate.toSqlDate(): Date = Date.valueOf(this.toString())

fun LocalDate.isWeekday(): Boolean =
    !(this.dayOfWeek == DayOfWeek.SUNDAY || this.dayOfWeek == DayOfWeek.SATURDAY)
