package com.example.f23hopper.utils.CalendarUtilities

import kotlinx.datetime.LocalDate
import java.sql.Date
import java.time.Instant
import java.time.ZoneId


fun java.time.LocalDate.toSqlDate(): Date =
    Date.valueOf(this.toString())

fun Date.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

fun java.util.Date.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

fun java.time.LocalDate.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

fun java.util.Date.toJavaLocalDate(): java.time.LocalDate {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
