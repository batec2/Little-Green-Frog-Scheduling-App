package com.example.f23hopper.data

enum class DayValidationError(@Suppress("unused") val displayMessage: String) {
    NO_SHIFTS("No shifts scheduled for the day."),
    INSUFFICIENT_SHIFTS("Not enough shifts for the day."),
    NO_DAY_OPENER("No opener scheduled for the day shift."),
    NO_NIGHT_CLOSER("No closer scheduled for the night shift."),
    NO_FULL_SHIFT_OPENER_CLOSER("No employees available for opening and closing in full shifts."),
    MISSING_DAY_SHIFT("No day shift scheduled."),
    MISSING_NIGHT_SHIFT("No night shift scheduled.")
}
