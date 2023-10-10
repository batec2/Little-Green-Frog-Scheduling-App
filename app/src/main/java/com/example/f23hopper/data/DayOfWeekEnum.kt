package com.example.f23hopper.data

import androidx.room.TypeConverter

enum class DayOfWeek {
    MONDAY {
        override fun toString() = "Monday"
    },
    TUESDAY {
        override fun toString() = "Tuesday"
    },
    WEDNESDAY {
        override fun toString() = "Wednesday"
    },
    THURSDAY {
        override fun toString() = "Thursday"
    },
    FRIDAY {
        override fun toString() = "Friday"
    },

    SATURDAY {
        override fun toString() = "Saturday"
    },
    SUNDAY {
        override fun toString() = "Sunday"
    };

    fun isWeekend(): Boolean = this == SATURDAY || this == SUNDAY
}

class WeekDayConverter {
    @TypeConverter
    fun toWeekDay(value: String) = enumValueOf<DayOfWeek>(value.lowercase())

    @TypeConverter
    fun fromWeekDay(value: DayOfWeek) = value.name.lowercase()
}