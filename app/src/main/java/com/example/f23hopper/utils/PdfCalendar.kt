package com.example.f23hopper.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

val backgroundColor = Color(0xFFFBFDF9)
val currentMonthDayColor = Color(0xFFCEE9DB)
val notCurrentMonthDayColor = Color(0xFFDADADA)
val offMonthTextColor = Color(0xFFA0A0A0)
val specialDayColor = Color(0xFFFDC559)
val textColor = Color(0xFF191C1A)
val morningShiftColor = Color(0xFF8795BE)
val eveningShiftColor = Color(0xFF664B7B)
val weekendShiftColor = Color(0xFF001E2F)
fun generatePdfCalendar(
    context: Context,
    yearMonth: YearMonth,
    shifts: List<Shift>,
    filename: String = "calendar.pdf",
    specialDays: List<SpecialDay>,
): File {
    val pageInfo = createPageInfo()
    val pdfDocument = PdfDocument()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = createTextPaint()

    drawMonthYearHeader(canvas, paint, yearMonth, pageInfo.pageWidth)

    drawDaysOfWeekHeaders(canvas, paint, pageInfo.pageWidth)

    val shiftsByDate = organizeShiftsByDate(shifts)
    val localSpecialDays = convertSpecialDaysToLocalDate(specialDays) // needed to id special days
    drawCalendarGridAndPopulate(
        canvas,
        paint,
        yearMonth,
        shiftsByDate,
        localSpecialDays,
        pageInfo.pageWidth
    )

    pdfDocument.finishPage(page)

    val file = savePdfToFile(pdfDocument, context, filename)
    pdfDocument.close()

    return file
}

private fun createPageInfo(): PdfDocument.PageInfo {
    return PdfDocument.PageInfo.Builder(595, 842, 1).create() //size of A4 paper
}

private fun createTextPaint(): Paint {
    return Paint().apply {
        textSize = 12f
    }
}


private fun drawDaysOfWeekHeaders(canvas: Canvas, paint: Paint, pageWidth: Int) {
    val daysOfWeek = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    val cellWidth = pageWidth / daysOfWeek.size
    val y = 80f // lower the weekday labels to make room for the "Month Year" header

    // set to bold
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

    daysOfWeek.forEachIndexed { index, day ->
        canvas.drawText(day, (index * cellWidth + cellWidth / 2).toFloat(), y, paint)
    }

    // reset text style to default
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
}

private fun organizeShiftsByDate(shifts: List<Shift>): Map<LocalDate, List<Shift>> {
    return shifts.groupBy { shift ->
        shift.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}

private fun drawMonthYearHeader(
    canvas: Canvas,
    paint: Paint,
    yearMonth: YearMonth,
    pageWidth: Int
) {
    paint.textSize = 30f // Larger text size for the header
    paint.textAlign = Paint.Align.CENTER // Align text in the center
    val monthYearString =
        "${yearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${yearMonth.year}"
    val x = pageWidth / 2f // Center position
    val y = 50f // Position for the header
    canvas.drawText(monthYearString, x, y, paint)
    paint.textSize = 12f // Reset text size for the rest of the document
    paint.textAlign = Paint.Align.LEFT // Reset text alignment
}


private fun drawEmployeeNames(
    canvas: Canvas,
    paint: Paint,
    x: Float,
    y: Float,
    date: LocalDate,
    cellHeight: Float,
    shiftsByDate: Map<LocalDate, List<Shift>>
) {
    shiftsByDate[date]?.let { shifts ->
        drawEmployeeNames(canvas, paint, x, y - cellHeight + 20f, shifts)
    }
}

private fun drawEmployeeNames(
    canvas: Canvas,
    paint: Paint,
    x: Float,
    y: Float,
    shifts: List<Shift>
) {
    val originalTextSize = paint.textSize
    val originalColor = paint.color
    paint.textSize = 10f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

    shifts.forEachIndexed { index, shift ->
        val employee = shift.employee

        val nameToUse = when {
            employee.nickname.isNotEmpty() -> employee.nickname
            else -> {
                val firstName = employee.firstName.take(2)
                val lastName = employee.lastName.take(8)
                "$firstName $lastName"
            }
        }

        val canOpen = employee.canOpen
        val canClose = employee.canClose
        val isDayShift = shift.schedule.shiftType == ShiftType.DAY
        val isNightShift = shift.schedule.shiftType == ShiftType.NIGHT
        val isFullShift = shift.schedule.shiftType == ShiftType.FULL

        val openSym = "🔑"
        val closeSym = "🔒"
        val lockSymbols = when {
            canOpen && canClose && isFullShift -> openSym + closeSym
            canOpen && isDayShift -> openSym
            canClose && isNightShift -> closeSym
            isDayShift || isNightShift -> "        " //space out to even the text
            else -> "      " //space out to even the text on full days
        }

        paint.color = when {
            isDayShift -> morningShiftColor.toArgb()
            isNightShift -> eveningShiftColor.toArgb()
            else -> weekendShiftColor.toArgb()
        }

        val textToDraw = "$lockSymbols $nameToUse"
        canvas.drawText(textToDraw, x + 10f, y + (index + 1) * 15f, paint)
    }

    // reset text style and color for the rest of the document
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.color = originalColor
    paint.textSize = originalTextSize
}

private fun drawCalendarGridAndPopulate(
    canvas: Canvas,
    paint: Paint,
    yearMonth: YearMonth,
    shiftsByDate: Map<LocalDate, List<Shift>>,
    specialDays: List<LocalDate>,
    pageWidth: Int
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val cellWidth = pageWidth / 7
    val cellHeight = 120f
    val offset = calculateMonthOffset(yearMonth)
    val previousMonthDays = yearMonth.minusMonths(1).lengthOfMonth()
    val nextMonthDays = yearMonth.plusMonths(1).lengthOfMonth()
    val totalCells = calculateTotalCells(daysInMonth, offset, yearMonth)

    var x: Float
    var y = 210f
    for (cellIndex in 0 until totalCells) {
        val day = calculateDay(cellIndex, offset, previousMonthDays, daysInMonth)
        val dayOfWeekIndex = cellIndex % 7
        x = (dayOfWeekIndex * cellWidth).toFloat() // where to start drawing day

        if (isNewWeek(dayOfWeekIndex, cellIndex)) y += cellHeight // new row if new week

        val date = calculateDate(yearMonth, day, cellIndex, offset, daysInMonth) // get LocalDate
        val isCurrentMonth = cellIndex >= offset && cellIndex < offset + daysInMonth

        // stop drawing if the current cell represents a day from the next month and the week is complete
        if (cellIndex >= offset + daysInMonth && dayOfWeekIndex == 0)
            break

        drawDayCell(
            canvas,
            paint,
            x,
            y,
            cellWidth.toFloat(),
            cellHeight,
            day,
            isCurrentMonth,
            date in specialDays
        )

        drawDateNumber(canvas, paint, x, y, day, cellHeight, isCurrentMonth)
        if (isCurrentMonth) {
            drawEmployeeNames(canvas, paint, x, y, date, cellHeight, shiftsByDate)
        }
    }
}

private fun calculateMonthOffset(yearMonth: YearMonth): Int {
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value
    return if (firstDayOfMonth == 7) 0 else firstDayOfMonth
}

private fun calculateDay(
    cellIndex: Int,
    offset: Int,
    previousMonthDays: Int,
    daysInMonth: Int
): Int {
    return when {
        cellIndex < offset -> previousMonthDays - offset + cellIndex + 1 // days from the previous month
        cellIndex < offset + daysInMonth -> cellIndex - offset + 1 // days from the current month
        else -> cellIndex - (offset + daysInMonth) + 1 // days from the next month
    }
}

private fun calculateDate(
    yearMonth: YearMonth,
    day: Int,
    cellIndex: Int,
    offset: Int,
    daysInMonth: Int
): LocalDate {
    return when {
        cellIndex < offset -> yearMonth.minusMonths(1).atDay(day) // dates from the previous month
        cellIndex < offset + daysInMonth -> yearMonth.atDay(day) // dates from the current month
        else -> yearMonth.plusMonths(1).atDay(day) // dates from the next month
    }
}

private fun isNewWeek(dayOfWeekIndex: Int, cellIndex: Int): Boolean {
    return dayOfWeekIndex == 0 && cellIndex > 0
}

private fun calculateTotalCells(daysInMonth: Int, offset: Int, yearMonth: YearMonth): Int {
    val endDayOfWeek = yearMonth.atEndOfMonth().dayOfWeek.value
    val endOffset =
        if (endDayOfWeek == 6) 0 else 7 - endDayOfWeek // Adjust endOffset to end on Saturday, thanks java library
    return daysInMonth + offset + endOffset
}

private fun drawDayCell(
    canvas: Canvas,
    paint: Paint,
    x: Float,
    y: Float,
    cellWidth: Float,
    cellHeight: Float,
    day: Int,
    isCurrentMonth: Boolean,
    isSpecialDay: Boolean
) {
    paint.color =
        if (isCurrentMonth) currentMonthDayColor.toArgb() else notCurrentMonthDayColor.toArgb()
    paint.style = Paint.Style.FILL
    canvas.drawRect(x, y - cellHeight, x + cellWidth, y, paint)

    if (isSpecialDay) {
        val padding = 1f
        paint.color = specialDayColor.toArgb()
        canvas.drawRect(
            x + padding,
            y - cellHeight + padding,
            x + cellWidth - padding,
            y - padding,
            paint
        )
    }

    paint.style = Paint.Style.STROKE
    paint.color = textColor.toArgb()
    canvas.drawRect(x, y - cellHeight, x + cellWidth, y, paint)
}

private fun drawDateNumber(
    canvas: Canvas,
    paint: Paint,
    x: Float,
    y: Float,
    day: Int,
    cellHeight: Float,
    isCurrentMonth: Boolean,
) {
    paint.color =
        if (isCurrentMonth) textColor.toArgb() else offMonthTextColor.toArgb() // Different color for off-month days
    paint.style = Paint.Style.FILL
    canvas.drawText(day.toString(), x + 10f, y - cellHeight + 20f, paint)
}

private fun savePdfToFile(pdfDocument: PdfDocument, context: Context, fileName: String): File {
    val file = File(context.getExternalFilesDir(null), fileName)
    FileOutputStream(file).use { out ->
        pdfDocument.writeTo(out)
    }
    return file
}

private fun convertSpecialDaysToLocalDate(specialDays: List<SpecialDay>): List<LocalDate> {
    return specialDays.map { specialDay ->
        specialDay.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}