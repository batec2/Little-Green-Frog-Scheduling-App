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
import com.example.f23hopper.ui.icons.contactMissingIcon
import com.example.f23hopper.ui.icons.rememberError
import com.example.f23hopper.ui.shiftedit.getEmployeeDisplayNameShort
import com.example.f23hopper.utils.CalendarUtilities.isWeekday
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toShortMonthAndDay
import com.example.f23hopper.utils.maxShifts
import java.time.LocalDate
import java.time.YearMonth

data class DayValidationResult(
    val isValid: Boolean,
    val errors: List<DayValidationError> = emptyList()
)

fun dateValidation(
    shifts: Map<ShiftType, List<Shift>>,
    date: LocalDate,
    isSpecialDay: Boolean
): DayValidationResult {
    val errors = mutableListOf<DayValidationError>()

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

    if (shifts[ShiftType.DAY]?.size != maxShifts(isSpecialDay) || shifts[ShiftType.DAY] == null)
        errors.add(DayValidationError.MISSING_DAY_SHIFT)

    if (shifts[ShiftType.NIGHT]?.size != maxShifts(isSpecialDay) || shifts[ShiftType.NIGHT] == null)
        errors.add(DayValidationError.MISSING_NIGHT_SHIFT)

    if (certifiedEmployeeAbsent(shifts, ShiftType.DAY, Employee::canOpen))
        errors.add(DayValidationError.NO_DAY_OPENER)

    if (certifiedEmployeeAbsent(shifts, ShiftType.NIGHT, Employee::canClose))
        errors.add(DayValidationError.NO_NIGHT_CLOSER)


    return errors
}

private fun certifiedEmployeeAbsent(
    shifts: Map<ShiftType, List<Shift>>,
    targetShiftType: ShiftType,
    isCertified: (Employee) -> Boolean,

    ): Boolean {
    if (shifts[targetShiftType].isNullOrEmpty()) {
        return true
    } else {
        shifts[targetShiftType]?.let { shiftsOfType ->
            if (shiftsOfType.none { isCertified(it.employee) }) {
                return true
            }
        }
    }
    return false
}

private fun weekendChecks(
    shifts: Map<ShiftType, List<Shift>>,
    isSpecialDay: Boolean
): List<DayValidationError> {
    val errors = mutableListOf<DayValidationError>()

    val weekendShifts = shifts[ShiftType.FULL]
    if (weekendShifts.isNullOrEmpty() || weekendShifts.size != maxShifts(isSpecialDay))
        errors.add(DayValidationError.INSUFFICIENT_SHIFTS_WEEKEND)

    if (certifiedEmployeeAbsent(shifts, ShiftType.FULL, Employee::canOpen))
        errors.add(DayValidationError.NO_WEEKEND_OPENER)

    if (certifiedEmployeeAbsent(shifts, ShiftType.FULL, Employee::canClose))
        errors.add(DayValidationError.NO_WEEKEND_CLOSER)

    return errors
}

@Composable
fun InvalidDayIcon(
    shifts: Map<ShiftType, List<Shift>>,
    date: LocalDate,
    isSpecialDay: Boolean,
    modifier: Modifier = Modifier,
    showDialogueOnClick: Boolean = false
) {
    // Early Exit, don't validate days in the past.
    if (date.isBefore(LocalDate.now())) return

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


fun getEmployeesWithNoShifts(
    shifts: List<Shift>,
    month: YearMonth,
    allEmployees: List<Employee>
): Map<LocalDate, List<Employee>> {
    val result = mutableMapOf<LocalDate, List<Employee>>()

    // convert the start day to LocalDate
    val firstDateOfMonth = month.atDay(1)
    val firstDayOfWeek = firstDateOfMonth.dayOfWeek.value

    // calculate the start date of the first week based on the first day of the month.
    // if the first day is a Sunday, start on that day, otherwise go back to the previous Sunday.
    var startDate = firstDateOfMonth.minusDays((firstDayOfWeek % 7).toLong())

    // keep iterating until we cover all days of the month.
    while (startDate.isBefore(month.atEndOfMonth().plusDays(1))) {
        val endDate = startDate.plusDays(6)

        // validate employees for the current week and add the result to the map.
        result.putAll(
            validateEmployeesForWeek(
                shifts,
                startDate,
                endDate,
                allEmployees
            )
        )

        // move to the next week.
        startDate = startDate.plusDays(7)
    }

    return result
}

fun validateEmployeesForWeek(
    shifts: List<Shift>,
    startDate: LocalDate,
    endDate: LocalDate,
    allEmployees: List<Employee>
): Map<LocalDate, List<Employee>> {
    val absentEmployees = allEmployees.toMutableList()

    // filter shifts by week, then remove employees present in the week, leaving absent employees
    shifts.filter { it.schedule.date.toJavaLocalDate() in startDate..endDate }
        .forEach { shift ->
            absentEmployees.remove(shift.employee)
        }

    return mapOf(startDate to absentEmployees)
}

@Composable
fun AbsentEmployeeIcon(
    shifts: List<Shift>,
    month: YearMonth,
    allEmployees: List<Employee>,
    modifier: Modifier = Modifier,
    onGenerationClick: () -> Unit,
    showDialogueOnClick: Boolean = true
) {
    val absentEmployeesByWeek = getEmployeesWithNoShifts(shifts, month, allEmployees)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ShowEmployeeAbsenceDialog(
            absentEmployees = absentEmployeesByWeek,
            onDismiss = { showDialog = false },
            onGenerationClick = onGenerationClick
        )
    }

    if (absentEmployeesByWeek.any { entry -> entry.value.isNotEmpty() }) {
        Icon(
            imageVector = contactMissingIcon(),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Employees Needing Shifts",
            modifier = modifier
                .padding(start = 14.dp, top = 5.dp)
                .size(28.dp)
                .then(
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
    absentEmployees: Map<LocalDate, List<Employee>>,
    onGenerationClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (absentEmployees.isNotEmpty()) {
        AlertDialog(

            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = onDismiss,
            title = { Text(text = "Missing Shifts:") },
            text = {
                Box(modifier = Modifier.padding(0.dp)) {
                    AbsentEmployeePager(
                        absentEmployees = absentEmployees
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onGenerationClick(); onDismiss() }) {
                        Text("Fix")
                    }
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
    absentEmployees: Map<LocalDate, List<Employee>>
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
            val weekStartDate = filteredAbsentEmployees.keys.sorted()[page]
            val weekEndDate = weekStartDate.plusDays(6)

            val displayText =
                "${weekStartDate.toShortMonthAndDay()} - ${weekEndDate.toShortMonthAndDay()}"

            val employeesForWeek = filteredAbsentEmployees[weekStartDate]

            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Top) {
                Text(
                    text = displayText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
                employeesForWeek?.forEach { employee ->
                    Text(
                        text = getEmployeeDisplayNameShort(employee),
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
