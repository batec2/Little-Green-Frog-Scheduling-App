package com.example.f23hopper.ui.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EmployeeEntryViewModel @Inject constructor(
    scheduleRepository: ScheduleRepository,
) : ViewModel() {

    private val _activeShiftsInFuture = MutableStateFlow<List<Shift>>(emptyList())
    private val activeShiftsInFuture: StateFlow<List<Shift>> = _activeShiftsInFuture

    init {
        viewModelScope.launch {
            scheduleRepository.getShiftsFromDate(LocalDate.now().toSqlDate()).collect {
                _activeShiftsInFuture.value = it
            }
        }
    }

    fun employeeOnlyOpenerCloserCheck(employeeUiState: EmployeeUiState): Boolean {
        val intendedChanges = employeeUiState.employeeDetails
        val employee = employeeUiState.employee ?: return false

        // check if either canOpen or canClose was turned ON
        if ((intendedChanges.canOpen && !employee.canOpen) || (intendedChanges.canClose && !employee.canClose)) {
            return false
        }

        // if both canOpen and canClose are true, return false
        if (intendedChanges.canClose && intendedChanges.canOpen) {
            return false
        }

        // if there are no active shifts in the future, return false
        if (activeShiftsInFuture.value.isEmpty()) return false
        val allShifts = activeShiftsInFuture.value

        // if there are no changes in canOpen and canClose, return false
        if (employee.canOpen == intendedChanges.canOpen && employee.canClose == intendedChanges.canClose) {
            return false
        }

        // Check if there are critical shifts
        return hasCriticalShifts(employee, allShifts)
    }


}

data class EmployeeUiState(
    val employee: Employee? = null,
    val employeeDetails: EmployeeDetails = EmployeeDetails(),
    val isEmployeeValid: Boolean = false
)

/**
 * Initializing the default values of Employee
 */
data class EmployeeDetails(
    val employeeId: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val nickname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    var canOpen: Boolean = false,
    var canClose: Boolean = false,
    var sunday: ShiftType = ShiftType.CANT_WORK,
    var monday: ShiftType = ShiftType.CANT_WORK,
    var tuesday: ShiftType = ShiftType.CANT_WORK,
    var wednesday: ShiftType = ShiftType.CANT_WORK,
    var thursday: ShiftType = ShiftType.CANT_WORK,
    var friday: ShiftType = ShiftType.CANT_WORK,
    var saturday: ShiftType = ShiftType.CANT_WORK,
    var active: Boolean = true
)

/**
 * Converts and Employee object to and Employee entity for the table
 */
fun EmployeeDetails.toEmployee(): Employee = Employee(
    employeeId = employeeId,
    firstName = firstName,
    lastName = lastName,
    nickname = nickname,
    email = email,
    phoneNumber = phoneNumber,
    canOpen = canOpen,
    canClose = canClose,
    sunday = sunday,
    monday = monday,
    tuesday = tuesday,
    wednesday = wednesday,
    thursday = thursday,
    friday = friday,
    saturday = saturday,
    active = active
)


fun Employee.toEmployeeUiState(isEmployeeValid: Boolean = true): EmployeeUiState = EmployeeUiState(
    employee = this,
    employeeDetails = this.toEmployeeDetails(),
    isEmployeeValid = isEmployeeValid
)

/**
 * Converts an Entry from the employee database to an EmployeeDetails object
 */
fun Employee.toEmployeeDetails(): EmployeeDetails = EmployeeDetails(
    employeeId = employeeId,
    firstName = firstName,
    lastName = lastName,
    nickname = nickname,
    email = email,
    phoneNumber = phoneNumber,
    canOpen = canOpen,
    canClose = canClose,
    sunday = sunday,
    monday = monday,
    tuesday = tuesday,
    wednesday = wednesday,
    thursday = thursday,
    friday = friday,
    saturday = saturday,
    active = active
)

fun determineShiftType(
    isWeekend: Boolean,
    shiftSelected: Boolean,
    dayShift: Boolean,
    nightShift: Boolean
): ShiftType {
    return if (isWeekend) {
        if (shiftSelected) ShiftType.FULL else ShiftType.CANT_WORK
    } else {
        when {
            dayShift && nightShift -> ShiftType.FULL
            dayShift -> ShiftType.DAY
            nightShift -> ShiftType.NIGHT
            else -> ShiftType.CANT_WORK
        }
    }
}

fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }.take(11)

    // US & Canada, e.g +1
    if (digits.startsWith("1") && digits.length == 11) {
        return "+1 (${digits.substring(1, 4)}) ${digits.substring(4, 7)}-${digits.substring(7)}"
    }

    // international numbers with country codes
    if (digits.length > 10) {
        // assumption: if longer than 10, it has country code
        return "+${digits.substring(0, digits.length - 10)} ${digits.substring(digits.length - 10)}"
    }

    // Default, CA/US without country code
    return when {
        digits.length <= 3 -> digits
        digits.length <= 6 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
        else -> "${digits.substring(0, 3)}-${digits.substring(3, 6)}-${digits.substring(6)}"
    }
}


fun formatName(input: String): String {
    return input.filter { it.isLetter() || it == '-' }
}

