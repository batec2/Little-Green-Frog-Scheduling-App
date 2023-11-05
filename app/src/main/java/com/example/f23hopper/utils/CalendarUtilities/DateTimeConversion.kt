package com.example.f23hopper.utils.CalendarUtilities

import kotlinx.datetime.LocalDate

object DateTimeFormatter {

    fun formatDate(date: LocalDate): String {
        val day = date.dayOfWeek
        val suffix = getDaySuffix(date.dayOfMonth)
        val month = date.month
        val year = date.year
        return "$day, $month ${date.dayOfMonth}$suffix, $year"
    }

    private fun getDaySuffix(day: Int): String {
        if (day in 11..13) return "th"
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}

