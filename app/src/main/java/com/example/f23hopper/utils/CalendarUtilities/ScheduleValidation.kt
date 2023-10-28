package com.example.f23hopper.utils.CalendarUtilities


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f23hopper.data.DayValidationError
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.isWeekday
import com.example.f23hopper.ui.calendar.maxShifts
import com.example.f23hopper.ui.icons.rememberError

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
