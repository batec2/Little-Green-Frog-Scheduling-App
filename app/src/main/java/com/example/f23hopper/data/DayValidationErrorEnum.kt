package com.example.f23hopper.data

import com.example.f23hopper.data.shifttype.ShiftType

sealed class DayValidationError {
    abstract val displayMessage: String
    abstract val shiftType: ShiftType?

    object NoDayOpener : DayValidationError() {
        override val displayMessage: String = "No opener scheduled for the day shift."
        override val shiftType: ShiftType = ShiftType.DAY
    }

    object NoNightCloser : DayValidationError() {
        override val displayMessage: String = "No closer scheduled for the night shift."

        override val shiftType: ShiftType = ShiftType.NIGHT
    }

    object NoWeekendOpener : DayValidationError() {
        override val displayMessage: String = "No opener scheduled for the weekend."

        override val shiftType: ShiftType = ShiftType.FULL
    }

    object NoWeekendCloser : DayValidationError() {
        override val displayMessage: String = "No closer scheduled for the weekend."

        override val shiftType: ShiftType = ShiftType.FULL
    }

    object InsufficientShiftsWeekend : DayValidationError() {
        override val displayMessage: String = "Not enough shifts scheduled for the weekend."
        override val shiftType: ShiftType = ShiftType.FULL
    }

    object MissingDayShift : DayValidationError() {
        override val displayMessage: String = "Not enough day shifts scheduled."
        override val shiftType: ShiftType = ShiftType.DAY
    }

    object MissingNightShift : DayValidationError() {
        override val displayMessage: String = "Not enough night shifts scheduled."

        override val shiftType: ShiftType = ShiftType.NIGHT
    }

    data class TooManyShiftsThisWeekMorning(val employeeNames: List<String>) :
        DayValidationError() {
        override val displayMessage: String =
            "The following employee/s have too many shifts scheduled this week:"
        // The employee names will be displayed in a list following this message.

        override val shiftType = null
    }

}
