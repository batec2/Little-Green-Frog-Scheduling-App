package com.example.f23hopper.data.shifttype

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class ShiftType {
    CANT_WORK {
        override fun toString(): String {
            return "Can't Work"
        }
    },
    DAY {
        override fun toString(): String {
            return "Day"
        }
    },
    NIGHT {
        override fun toString(): String {
            return "Night"
        }
    },
    FULL {
        override fun toString(): String {
            return "Full"
        }
    }
}

class ShiftTypeConverter {
    @TypeConverter
    fun toShiftType(value: String) = enumValueOf<ShiftType>(value)

    @TypeConverter
    fun fromShiftType(value: ShiftType) = value.name
}

@Entity(tableName = "ShiftTypeIDs")
data class ShiftTypeID(
    @PrimaryKey val id: Int,
    val type: ShiftType
)
