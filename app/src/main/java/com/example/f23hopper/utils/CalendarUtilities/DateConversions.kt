package com.example.f23hopper.utils.CalendarUtilities

import kotlinx.datetime.LocalDate
import java.sql.Date
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.util.Locale


fun java.time.LocalDate.toSqlDate(): Date = Date.valueOf(this.toString())

fun java.util.Date.toSqlDate(): Date {
    return Date(this.time)
}

fun Date.toKotlinxLocalDate(): LocalDate = LocalDate.parse(this.toString())
fun java.util.Date.toKotlinxLocalDate(): LocalDate = LocalDate.parse(this.toString())
fun java.time.LocalDate.toKotlinxLocalDate(): LocalDate = LocalDate.parse(this.toString())
fun java.util.Date.toJavaLocalDate(): java.time.LocalDate {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun java.time.LocalDate.toShortMonthAndDay(): String {
    return "${
        this.month.getDisplayName(
            java.time.format.TextStyle.SHORT,
            Locale.getDefault()
        )
    } ${this.dayOfMonth}"
}

fun Date.toLongDate(): String {
    val date = this.toKotlinxLocalDate()
    val dayNum = date.dayOfMonth
    val day = date.dayOfWeek.toString().lowercase().replaceFirstChar { it.titlecase() }
    val month = date.month.toString().lowercase().replaceFirstChar { it.titlecase() }
    val year = date.year
    val suffix = if (dayNum in 11..13) "th" else {
        when (dayNum % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
    return "$day, $month ${date.dayOfMonth}$suffix, $year"
}

fun DayOfWeek.isWeekend(): Boolean = (this == DayOfWeek.SATURDAY || this == DayOfWeek.SUNDAY)

fun java.time.LocalDate.isWeekday() =
    !(this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY)

fun LocalDate.toSqlDate(): Date = Date.valueOf(this.toString())

fun LocalDate.isWeekday(): Boolean =
    !(this.dayOfWeek == DayOfWeek.SUNDAY || this.dayOfWeek == DayOfWeek.SATURDAY)

fun java.time.LocalDate.datesUntil(endExclusive: java.time.LocalDate): Sequence<java.time.LocalDate> {
    return generateSequence(this) { currentDate ->
        currentDate.plusDays(1).takeIf { it.isBefore(endExclusive) }
    }
}
