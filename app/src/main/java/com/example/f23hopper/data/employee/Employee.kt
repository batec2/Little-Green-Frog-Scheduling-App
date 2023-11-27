package com.example.f23hopper.data.employee

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.f23hopper.data.shifttype.ShiftType
import java.time.DayOfWeek

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    var employeeId: Long = 0,
    var active: Boolean,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val maxShifts: String,
    var canOpen: Boolean,
    var canClose: Boolean,
    var sunday: ShiftType,
    var monday: ShiftType,
    var tuesday: ShiftType,
    var wednesday: ShiftType,
    var thursday: ShiftType,
    var friday: ShiftType,
    var saturday: ShiftType,
)

fun Employee.shiftTypeForDay(dayOfWeek: DayOfWeek): ShiftType {
    return when (dayOfWeek) {
        DayOfWeek.SUNDAY -> this.sunday
        DayOfWeek.MONDAY -> this.monday
        DayOfWeek.TUESDAY -> this.tuesday
        DayOfWeek.WEDNESDAY -> this.wednesday
        DayOfWeek.THURSDAY -> this.thursday
        DayOfWeek.FRIDAY -> this.friday
        DayOfWeek.SATURDAY -> this.saturday
    }
}

fun Employee.availableDays(): List<DayOfWeek> {
    val availability = mutableListOf<DayOfWeek>()

    if (sunday != ShiftType.CANT_WORK) availability.add(DayOfWeek.SUNDAY)
    if (monday != ShiftType.CANT_WORK) availability.add(DayOfWeek.MONDAY)
    if (tuesday != ShiftType.CANT_WORK) availability.add(DayOfWeek.TUESDAY)
    if (wednesday != ShiftType.CANT_WORK) availability.add(DayOfWeek.WEDNESDAY)
    if (thursday != ShiftType.CANT_WORK) availability.add(DayOfWeek.THURSDAY)
    if (friday != ShiftType.CANT_WORK) availability.add(DayOfWeek.FRIDAY)
    if (saturday != ShiftType.CANT_WORK) availability.add(DayOfWeek.SATURDAY)

    return availability
}
