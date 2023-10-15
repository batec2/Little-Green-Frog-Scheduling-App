package com.example.f23hopper.ui.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.shifttype.ShiftType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmployeeEntryViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
) : ViewModel() {
    var employeeUiState by mutableStateOf(EmployeeUiState())
        private set

    //updates current employee details
    fun updateUiState(employeeDetails: EmployeeDetails) {
        employeeUiState =
            EmployeeUiState(
                employeeDetails = employeeDetails,
                isEmployeeValid = validateInput(employeeDetails)
            )
    }
    //checks if fields: firstName,lastName,email,phone number is not blank
    private fun validateInput(uiState: EmployeeDetails = employeeUiState.employeeDetails): Boolean {
        return with(uiState) {
            firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
                    && phoneNumber.isNotBlank()
        }
    }
    //inserts employee details into database
    suspend fun saveEmployee() {
        if (validateInput()) { //checks if inputs are not blank
            employeeRepository.insertEmployee(employeeUiState.employeeDetails.toEmployee())
        }
    }
}

data class EmployeeUiState(
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
    var saturday: ShiftType = ShiftType.CANT_WORK
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
    saturday = saturday
)


fun Employee.toEmployeeUiState(isEmployeeValid: Boolean = true): EmployeeUiState = EmployeeUiState(
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
    saturday = saturday
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
    val digits = input.filter { it.isDigit() }

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

