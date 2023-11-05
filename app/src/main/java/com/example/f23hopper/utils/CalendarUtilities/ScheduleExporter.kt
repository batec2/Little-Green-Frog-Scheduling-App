package com.example.f23hopper.utils.CalendarUtilities

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.f23hopper.data.schedule.Shift
import java.io.File
import java.time.YearMonth

class ScheduleExporter {

    fun formatFileData(shifts: List<Shift>, curMonth: YearMonth): String {
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

            val rows = shiftsOnDate.joinToString("\n") { shift ->
                val fullName = "${shift.employee.firstName} ${shift.employee.lastName}"
                val shiftType = "${shift.schedule.shiftType} shift:"
                "%-15s %-30s".format(shiftType, fullName)
            }

            "$formattedDate\n$hyphens\n$rows\n"
        }

        return formattedShifts

    }

    fun createFile(content: String, context: Context, filename: String = "schedule.txt"): File {
        val file = File(context.cacheDir, filename)
        file.writeText(content)
        return file
    }

    fun shareFile(file: File, context: Context) {
        val downloadsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadsDirectory, file.name)

        try {
            file.copyTo(destinationFile, true)
            val toast = Toast.makeText(
                context,
                "${file.name} saved to Downloads folder",
                Toast.LENGTH_SHORT
            )
            toast.show()
        } catch (e: Exception) {
            Log.e("Error", "Error copying file: ${e.message}")
            Toast.makeText(context, "Failed to save schedule", Toast.LENGTH_SHORT).show()
        }
    }
}