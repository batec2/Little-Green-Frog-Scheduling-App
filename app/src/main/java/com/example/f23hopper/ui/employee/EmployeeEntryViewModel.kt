package com.example.f23hopper.ui.employee

import EmployeeRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.f23hopper.data.Employee
import com.example.f23hopper.data.ShiftType

class EmployeeEntryViewModel(private val employeeRepository: EmployeeRepository) : ViewModel() {
    var employeeUiState by mutableStateOf(EmployeeUiState())
        private set

    fun updateUiState(employeeDetails: EmployeeDetails) {
        employeeUiState =
            EmployeeUiState(employeeDetails = employeeDetails, isEmployeeValid = validateInput(employeeDetails))
    }

    private fun validateInput(uiState: EmployeeDetails = employeeUiState.employeeDetails): Boolean {
        return with(uiState) {
            firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
                    && phoneNumber.isNotBlank()
        }
    }

    suspend fun saveEmployee(){
        if(validateInput()){ //checks if inputs are not blank
            employeeRepository.insertEmployee(employeeUiState.employeeDetails.toEmployee())
        }
    }
}

class EmployeeEntryViewModelFactory(private val repository: EmployeeRepository) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmployeeEntryViewModel(repository) as T
    }
}


data class EmployeeUiState(
    val employeeDetails: EmployeeDetails = EmployeeDetails(),
    val isEmployeeValid: Boolean = false
)

data class EmployeeDetails(
    val employeeId: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
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

fun EmployeeDetails.toEmployee(): Employee = Employee(
    employeeId = employeeId,
    firstName = firstName,
    lastName = lastName,
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

fun Employee.toEmployeeUiState(isEmployeeValid: Boolean = false): EmployeeUiState = EmployeeUiState(
    employeeDetails = this.toEmployeeDetails(),
    isEmployeeValid = isEmployeeValid
)

fun Employee.toEmployeeDetails(): EmployeeDetails = EmployeeDetails(
    employeeId = employeeId,
    firstName = firstName,
    lastName = lastName,
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