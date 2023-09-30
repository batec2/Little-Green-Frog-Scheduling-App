package com.example.f23hopper.data

import androidx.room.TypeConverter

enum class WeekDay {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY
}

class WeekDayConverter {
    @TypeConverter
    fun toWeekDay(value: String) = enumValueOf<WeekDay>(value.lowercase())

    @TypeConverter
    fun fromWeekDay(value: WeekDay) = value.name.lowercase()
}