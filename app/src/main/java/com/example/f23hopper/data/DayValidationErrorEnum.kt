package com.example.f23hopper.data

import com.example.f23hopper.data.shifttype.ShiftType

enum class DayValidationError(val displayMessage: String, val shiftType: ShiftType?) {
    NO_SHIFTS("No shifts scheduled for the day.", null),
    INSUFFICIENT_SHIFTS("Not enough shifts for the day.", null),
    NO_DAY_OPENER("No opener scheduled for the day shift.", ShiftType.DAY),
    NO_NIGHT_CLOSER("No closer scheduled for the night shift.", ShiftType.NIGHT),
    NO_FULL_SHIFT_OPENER_CLOSER(
        "No employees available for opening and closing in full shifts.",
        ShiftType.FULL
    ),
    MISSING_DAY_SHIFT("Not enough day shifts scheduled.", ShiftType.DAY),
    MISSING_NIGHT_SHIFT("Not enough night shifts scheduled.", ShiftType.NIGHT)
}
