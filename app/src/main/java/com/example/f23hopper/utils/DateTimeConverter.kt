package com.example.f23hopper.utils

import androidx.room.TypeConverter
import java.sql.Date

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
