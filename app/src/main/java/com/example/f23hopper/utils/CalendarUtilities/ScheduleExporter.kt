package com.example.f23hopper.utils.CalendarUtilities

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftTypeConverter
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.utils.generatePdfCalendar
import java.io.File
import java.time.YearMonth

class ScheduleExporter {

    fun formatSchedule(shifts: List<Shift>, curMonth: YearMonth): String {

        val filteredShifts = shifts.filter { shift ->
            val scheduleDate = shift.schedule.date.toKotlinxLocalDate()
            scheduleDate.year == curMonth.year && scheduleDate.month == curMonth.month
        }
            .groupBy { it.schedule.date }
            .toList()
            .sortedBy { it.first }

        val formattedSchedule = filteredShifts.joinToString("\n") { (date, shiftsOnDate) ->
            val formattedDate = date.toLongDate()
            val hyphens = "${"_".repeat(30)}\n"
            val rows = formatRows(shiftsOnDate)
            "\n$formattedDate\n$hyphens\n$rows\n"
        }

        return formattedSchedule
    }

    private fun formatRows(shifts: List<Shift>): String {

        val sortedShifts = shifts.sortedWith(
                 compareBy<Shift> { it.schedule.shiftType }
                .thenByDescending { it.employee.canOpen }
                .thenBy           { it.employee.canClose }
        )

        val rows = sortedShifts.joinToString("\n") { shift ->
            val type = ShiftTypeConverter().fromShiftType(shift.schedule.shiftType)
            val training = when {
                type == "DAY" && shift.employee.canOpen -> " (O)"
                type == "NIGHT" && shift.employee.canClose -> " (C)"
                type == "FULL" && shift.employee.canOpen && shift.employee.canClose -> " (O, C)"
                type == "FULL" && shift.employee.canOpen -> " (O)"
                type == "FULL" && shift.employee.canClose -> " (C)"
                else -> ""
            }
            val fullName = "${shift.employee.firstName} ${shift.employee.lastName}${training}"
            val shiftType = "${shift.schedule.shiftType} shift:"
            "%-15s %-30s".format(shiftType, fullName)
        }

        return rows
    }

    private fun shareFile(filename: String, content: String): String {

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        var file = File(downloadsDir, filename)

        Log.d("content", content)
        var count = 1
        while (file.exists()) {

            val base      = filename.substring(0, filename.lastIndexOf("."))
            val extension = filename.substring(filename.lastIndexOf(".") + 1)

            // Make a copy of the file if it already exists.
            val newFilename = "${base}($count).${extension}"
            file = File(downloadsDir, newFilename)
            count++
        }

        file.writeText(content)
        return file.name
    }

    private fun shareFile(file: File) {

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val destinationFile = File(downloadsDir, file.name)

        file.copyTo(destinationFile, overwrite = true)
    }

    fun export(
        filename: String,
        content: String,
        context: Context,
        shifts: List<Shift>,
        specialDays: List<SpecialDay>,
        month: YearMonth,
    ) {
        try {
            val pdf = generatePdfCalendar(
                context = context,
                filename = "$filename.pdf",
                yearMonth = month,
                shifts = shifts,
                specialDays = specialDays
            )
            shareFile("$filename.txt", content)
            shareFile(pdf)
        } catch (e: Exception) {
            Log.e("Error", "Error saving or sharing file: ${e.message}")
        }
    }
}