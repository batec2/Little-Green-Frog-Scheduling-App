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

class ScheduleExporter(
    private val context: Context,
    private val shifts: List<Shift>,
    private val specialDays: List<SpecialDay>,
    private val curMonth: YearMonth
) {

    private var basename = "${curMonth.year}_${curMonth.month.value}_schedule"
    private var count = 1
    private val content = formatSchedule()

    private fun formatSchedule(): String {

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

    private fun formatRows(shiftsOnDate: List<Shift>): String {

        val sortedShifts = shiftsOnDate.sortedWith(
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

    private fun updateBasename(extension: String) {

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        var file = File(downloadsDir, "$basename.$extension")

        while (file.exists()) {
            file = File(downloadsDir, "${basename}($count).$extension")
            count++
        }
        basename = file.nameWithoutExtension

    }

    private fun shareFile() {
        updateBasename("txt")
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val destinationFile = File(downloadsDir, "$basename.txt")
        destinationFile.writeText(content)

    }
    private fun shareFile(file: File) {
        updateBasename("pdf")
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadsDir, "$basename.pdf")
        file.copyTo(destinationFile)
    }

    fun export() {
        try {
            val pdf = generatePdfCalendar(
                context = context,
                filename = "$basename.pdf",
                yearMonth = curMonth,
                shifts = shifts,
                specialDays = specialDays
            )
            shareFile()
            shareFile(pdf)
        } catch (e: Exception) {
            Log.e("Error", "Error saving or sharing file: ${e.message}")
        }
    }
}