package com.example.f23hopper.data.shifttype

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class ShiftType {
    CANT_WORK,
    DAY,
    NIGHT,
    FULL
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