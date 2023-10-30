
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f23hopper.data.DayValidationError
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.icons.rememberError
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.isWeekday
import com.example.f23hopper.utils.maxShifts
import java.time.YearMonth

data class DayValidationResult(
    val isValid: Boolean,
    val errors: List<DayValidationError> = emptyList()
)

fun dateValidation(
    shifts: Map<ShiftType, List<Shift>>,
    date: java.time.LocalDate,
    isSpecialDay: Boolean
): DayValidationResult {
    val errors = mutableListOf<DayValidationError>()

    if (shifts.isEmpty()) {
        errors.add(DayValidationError.NO_SHIFTS)
    }

    if (date.isWeekday()) {
        errors.addAll(weekdayChecks(shifts, isSpecialDay))
    } else {
        errors.addAll(weekendChecks(shifts, isSpecialDay))
    }

    return if (errors.isEmpty()) {
        DayValidationResult(isValid = true)
    } else {
        DayValidationResult(isValid = false, errors = errors)
    }
}

private fun weekdayChecks(
    shifts: Map<ShiftType, List<Shift>>,
    isSpecialDay: Boolean
): List<DayValidationError> {
    val errors = mutableListOf<DayValidationError>()

    if (shifts[ShiftType.DAY]?.size != maxShifts(isSpecialDay) || shifts[ShiftType.DAY] == null) {
        errors.add(DayValidationError.MISSING_DAY_SHIFT)
    }
    if (shifts[ShiftType.NIGHT]?.size != maxShifts(isSpecialDay) || shifts[ShiftType.NIGHT] == null) {
        errors.add(DayValidationError.MISSING_NIGHT_SHIFT)
    }

    shifts[ShiftType.DAY]?.let { dayShifts ->
        if (dayShifts.none { it.employee.canOpen }) {
            errors.add(DayValidationError.NO_DAY_OPENER)
        }
    }

    if (shifts[ShiftType.NIGHT].isNullOrEmpty()) {
        errors.add(DayValidationError.NO_NIGHT_CLOSER)
    } else {
        shifts[ShiftType.NIGHT]?.let { nightShifts ->
            if (nightShifts.none { it.employee.canClose }) {
                errors.add(DayValidationError.NO_NIGHT_CLOSER)
            }
        }
    }
    return errors
}

private fun weekendChecks(
    shifts: Map<ShiftType, List<Shift>>,
    isSpecialDay: Boolean
): List<DayValidationError> {
    val errors = mutableListOf<DayValidationError>()

    if (shifts[ShiftType.FULL]?.size != maxShifts(isSpecialDay)) {
        errors.add(DayValidationError.INSUFFICIENT_SHIFTS)
    }

    shifts[ShiftType.FULL]?.let { fullShifts ->
        val canOpenEmployee = fullShifts.find { it.employee.canOpen }
        val canCloseEmployee = fullShifts.find { it.employee.canClose }
        val hasBoth = fullShifts.any { it.employee.canOpen && it.employee.canClose }
        if (!hasBoth && (canOpenEmployee == null || canCloseEmployee == null || canOpenEmployee.employee.employeeId == canCloseEmployee.employee.employeeId)) {
            errors.add(DayValidationError.NO_FULL_SHIFT_OPENER_CLOSER)
        }
    }

    return errors
}

@Composable
fun InvalidDayIcon(
    shifts: Map<ShiftType, List<Shift>>,
    date: java.time.LocalDate,
    isSpecialDay: Boolean,
    modifier: Modifier = Modifier,
    showDialogueOnClick: Boolean = false
) {
    val dayValidation = dateValidation(shifts, date, isSpecialDay)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ShowErrorDialog(
            errors = dayValidation.errors,
            onDismiss = { showDialog = false }
        )
    }

    if (!dayValidation.isValid) {
        Icon(
            imageVector = rememberError(),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = dayValidation.errors.joinToString(", "),
            modifier = modifier.then(
                if (showDialogueOnClick) {
                    Modifier.clickable(onClick = { showDialog = true })
                } else {
                    Modifier
                }
            )
        )
    }
}


@Composable
fun ShowErrorDialog(errors: List<DayValidationError>, onDismiss: () -> Unit) {
    if (errors.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Shift Errors:") },
            text = {
                Column {
                    ShiftType.values().forEach { shiftType ->
                        val errorsForShift = errors.filter { it.shiftType == shiftType }
                        if (errorsForShift.isNotEmpty()) {
                            if (shiftType != ShiftType.FULL) {
                                Box {
                                    Text(
                                        text = "${shiftType.name}:",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            errorsForShift.forEach { error ->
                                Text(
                                    text = "- " + error.displayMessage,
                                    style = TextStyle(fontSize = 16.sp),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        )
    }
}

fun getWeekOfMonth(startDay: Int, month: YearMonth): WeekOfMonth {
    val firstDayOfWeek = month.atDay(1).dayOfWeek.value
    val weekNumber =
        if (firstDayOfWeek == 7 && startDay <= 7) {  // If the month starts on Sunday and we're looking at the first week
            1
        } else if (firstDayOfWeek == 7) {
            (startDay + 6) / 7
        } else {
            (startDay + firstDayOfWeek - 2) / 7 + 1
        }

    return when (weekNumber) {
        1 -> WeekOfMonth.WEEK1
        2 -> WeekOfMonth.WEEK2
        3 -> WeekOfMonth.WEEK3
        4 -> WeekOfMonth.WEEK4
        else -> WeekOfMonth.WEEK5
    }
}

enum class WeekOfMonth {
    WEEK1, WEEK2, WEEK3, WEEK4, WEEK5
}

fun validateEmployeesForWeek(
    shifts: List<Shift>,
    startDay: Int,
    endDay: Int,
    allEmployees: List<Employee>,
    month: YearMonth
): Map<WeekOfMonth, List<Employee>> {
    val absentEmployees = allEmployees.toMutableList()

    // Determine starting and ending months (in case a week crosses over both)
    val startDayMonth = if (startDay < 1) month.minusMonths(1) else month
    val endDayMonth = if (endDay > month.lengthOfMonth()) month.plusMonths(1) else month
    val startDate =
        startDayMonth.atDay(if (startDay < 1) month.lengthOfMonth() + startDay else startDay)
    val endDate =
        endDayMonth.atDay(if (endDay > month.lengthOfMonth()) endDay - month.lengthOfMonth() else endDay)

    // Filter shifts and by week, then remove employees present in the week, leaving absent employees
    shifts.filter { it.schedule.date.toJavaLocalDate() in startDate..endDate }
        .forEach { shift ->
            absentEmployees.remove(shift.employee)
        }

    val weekOfMonth = getWeekOfMonth(startDay, month)

    return mapOf(weekOfMonth to absentEmployees)
}


fun validateMonthForEmployeeAbsence(
    shifts: List<Shift>,
    month: YearMonth,
    allEmployees: List<Employee>
): Map<WeekOfMonth, List<Employee>> {
    val result = mutableMapOf<WeekOfMonth, List<Employee>>()

    // determine the day of the week for the first day of the month.
    // e.g. if the month starts on a Wednesday, `firstDayOfWeek` would be 3.
    // Monday = 1, Tuesday = 2, ..., Sunday = 7.
    val firstDayOfWeek = month.atDay(1).dayOfWeek.value

    // calculate the start day of the first week based on the first day of the month.
    // modulo operation makes it so Sunday is treated as day 0.
    // e.g.: if the month starts on a Tuesday (2), `startDay` will be -1.
    // this means the first week starts 2 days  before the month starts (previous Sunday).
    // (day -1..day 0..day 1 (start of month) )
    var startDay = 1 - (firstDayOfWeek % 7)

    // keep iterating until we cover all days of the month.
    while (startDay <= month.lengthOfMonth()) {
        // calc the end day for the current week being processed.
        val endDay = startDay + 6

        // validate employees for the current week and add the result to the map.
        result.putAll(
            validateEmployeesForWeek(
                shifts,
                startDay,
                endDay,
                allEmployees,
                month = month
            )
        )

        // Move to the next week.
        startDay += 7
    }

    return result
}

@Composable
fun AbsentEmployeeIcon(
    shifts: List<Shift>,
    month: YearMonth,
    allEmployees: List<Employee>,
    modifier: Modifier = Modifier,
    showDialogueOnClick: Boolean = true
) {
    val absentEmployeesByWeek = validateMonthForEmployeeAbsence(shifts, month, allEmployees)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ShowEmployeeAbsenceDialog(
            absentEmployees = absentEmployeesByWeek,
            onDismiss = { showDialog = false }
        )
    }

    if (absentEmployeesByWeek.isNotEmpty()) {
        Icon(
            imageVector = rememberError(),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Employees absent for the month",
            modifier = modifier.then(
                if (showDialogueOnClick) {
                    Modifier.clickable(onClick = { showDialog = true })
                } else {
                    Modifier
                }
            )
        )
    }
}

@Composable
fun ShowEmployeeAbsenceDialog(
    absentEmployees: Map<WeekOfMonth, List<Employee>>,
    onDismiss: () -> Unit
) {
    if (absentEmployees.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Absent Employees:") },
            text = {
                Box(modifier = Modifier.padding(0.dp)) {
                    AbsentEmployeePager(
                        absentEmployees = absentEmployees
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbsentEmployeePager(
    absentEmployees: Map<WeekOfMonth, List<Employee>>
) {
    // no need to show weeks with absent employees
    val filteredAbsentEmployees = absentEmployees.filterValues { it.isNotEmpty() }

    // calculating the height based on the week with the maximum number of absent employees
    val maxAbsents = filteredAbsentEmployees.values.maxByOrNull { it.size }?.size ?: 0
    val estimatedHeight =
        (maxAbsents * 24).dp + 70.dp  // assuming 24.dp per employee and adding some padding

    val pagerState = rememberPagerState(initialPage = 0)
    Column(modifier = Modifier.height(estimatedHeight)) {

        HorizontalPager(pageCount = filteredAbsentEmployees.size, state = pagerState) { page ->
            val week = filteredAbsentEmployees.keys.toList()[page]
            val employeesForWeek = filteredAbsentEmployees[week]

            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Top) {
                Text(
                    text = "$week:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
                employeesForWeek?.forEach { employee ->
                    Text(
                        text = "- ${employee.firstName} ${employee.lastName}",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 10.dp, start = 0.dp)
                    )
                }
            }
        }
    }

    // Dots were acting weird when they were in the column
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(filteredAbsentEmployees.size) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(5.dp)
            )
        }
    }
}
