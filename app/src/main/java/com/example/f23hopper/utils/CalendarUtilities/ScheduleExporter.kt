package com.example.f23hopper.utils.CalendarUtilities

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.shifttype.ShiftTypeConverter
import java.io.File
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class ScheduleExporter {

    fun formatData(shifts: List<Shift>, curMonth: YearMonth): String {
        val filteredShifts = shifts.filter { shift ->
            val scheduleDate = shift.schedule.date.toKotlinxLocalDate()
            scheduleDate.year == curMonth.year && scheduleDate.month == curMonth.month
        }

        val groupedShifts = filteredShifts.groupBy { it.schedule.date }
            .toList()
            .sortedBy { it.first }

        val formattedShifts = groupedShifts.joinToString("\n") { (date, shiftsOnDate) ->
            val formattedDate = date.toLongDate()
            val hyphens = "${"_".repeat(30)}\n"
            val rows = getRows(shiftsOnDate)
            "$formattedDate\n$hyphens\n$rows\n"
        }

        return formattedShifts
    }

    private fun getRows(shifts: List<Shift>): String {
        var openerFound = false
        var closerFound = false

        val (canOpenShifts, otherShifts) = shifts.partition {
            it.employee.canOpen && it.schedule.shiftType == ShiftType.DAY
        }
        val (canCloseShifts, remainingShifts) = otherShifts.partition {
            it.employee.canClose && it.schedule.shiftType == ShiftType.NIGHT
        }

        val rows = (canOpenShifts + remainingShifts + canCloseShifts).joinToString("\n") { shift ->
            val type = ShiftTypeConverter().fromShiftType(shift.schedule.shiftType)
            val training = when {
                type == "DAY" && !openerFound && shift.employee.canOpen -> {
                    openerFound = true; " (O)"
                }
                type == "NIGHT" && !closerFound && shift.employee.canClose -> {
                    closerFound = true; " (C)"
                }
                type == "FULL" && !openerFound && shift.employee.canOpen -> {
                    openerFound = true; " (O)"
                }
                type == "FULL" && !closerFound && shift.employee.canClose -> {
                    closerFound = true; " (C)"
                }
                else -> ""
            }
            val fullName = "${shift.employee.firstName} ${shift.employee.lastName}${training}"
            val shiftType = "${shift.schedule.shiftType} shift:"
            "%-15s %-30s".format(shiftType, fullName)
        }

        return rows
    }

    private fun shareFile(curMonth: YearMonth, content: String, context: Context) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val filename = "${curMonth.year}_${curMonth.month.value}_schedule.txt"
        val file = File(downloadsDir, filename)

        file.writeText(content)
        val toast = Toast.makeText(
            context,
            "$filename saved to Downloads folder",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun export(curMonth: YearMonth, content: String, context: Context) {
        try {
            shareFile(curMonth, content, context)
        } catch (e: Exception) {
            Log.e("Error", "Error saving or sharing file: ${e.message}")
            Toast.makeText(context, "Failed to save or share schedule", Toast.LENGTH_SHORT).show()
        }
    }
}