package com.example.f23hopper.data

import com.example.f23hopper.data.shifttype.ShiftType

enum class DayValidationError(val displayMessage: String, val shiftType: ShiftType?) {
    NO_DAY_OPENER("No opener scheduled.", ShiftType.DAY),
    NO_NIGHT_CLOSER("No closer scheduled.", ShiftType.NIGHT),
    NO_WEEKEND_OPENER("No opener scheduled.", ShiftType.FULL),
    NO_WEEKEND_CLOSER("No closer scheduled.", ShiftType.FULL),
    INSUFFICIENT_SHIFTS_WEEKEND("Not enough shifts scheduled", ShiftType.FULL),
    MISSING_DAY_SHIFT("Not enough day shifts scheduled.", ShiftType.DAY),
    MISSING_NIGHT_SHIFT("Not enough night shifts scheduled.", ShiftType.NIGHT)
}
