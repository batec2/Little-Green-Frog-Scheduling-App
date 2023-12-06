package com.example.f23hopper.ui.employee

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.shifttype.ShiftType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    val employeeRepository: EmployeeRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    var employees by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())

    var showConfirmationDialog by mutableStateOf(false)
        private set

    var employeeToToggle: Employee? by mutableStateOf(null)
        private set

    var selection by mutableStateOf("All Employees");

    fun toggleEmployeeActive(employee: Employee, value: Boolean) {
        viewModelScope.launch {
            val active: Int = if (value) 0 else 1
            if (active == 0) {
                // collect future shifts
                val futureShifts =
                    scheduleRepository.getShiftsFromDate(Date(System.currentTimeMillis())).first()
                val hasFutureShifts =
                    futureShifts.any { it.employee.employeeId == employee.employeeId }

                if (hasFutureShifts) {
                    showConfirmationDialog = true
                    employeeToToggle = employee
                } else {
                    employeeRepository.updateEmployeeActive(employee, active)
                }
            } else {
                // reactivate
                employeeRepository.updateEmployeeActive(employee, active)
            }
        }
    }

    fun confirmDeactivation() {
        viewModelScope.launch {
            employeeToToggle?.let {
                employeeRepository.updateEmployeeActive(it, 0)
            }
            // Reset state
            showConfirmationDialog = false
            employeeToToggle = null
        }
    }

    fun dismissDialog() {
        showConfirmationDialog = false
        employeeToToggle = null
    }

    var employeeUiState by mutableStateOf(EmployeeUiState())
        private set

    fun updateUiState(employeeDetails: EmployeeDetails) {
        employeeUiState = EmployeeUiState(
            employee = employeeUiState.employee,
            employeeDetails = employeeDetails,
            isEmployeeValid = validateInput(employeeDetails, employees.value ?: emptyList())
        )
    }

    fun resetUiState() {
        employeeUiState = EmployeeUiState(
            employee = employeeUiState.employee,
            employeeDetails = EmployeeDetails(),
            isEmployeeValid = false,
        )
    }

    fun filterEmployee(filter: String) {
        employees = when (filter) {
            "All Employees" -> employeeRepository.getAllActiveEmployees().asLiveData()
            "Can Open" -> employeeRepository.getCanOpen().asLiveData()
            "Can Close" -> employeeRepository.getCanClose().asLiveData()
            "Can Work Weekend" -> employeeRepository.getCanWorkWeekends().asLiveData()
            "Inactive" -> employeeRepository.getInactiveEmployees().asLiveData()
            else -> employeeRepository.getAllActiveEmployees().asLiveData()
        }
    }


    fun saveEmployee() {
        viewModelScope.launch {
            // Check if inputs are valid
            if (validateInput(employeeUiState.employeeDetails, employees.value ?: emptyList())) {
                // Insert the employee with the new nickname
                employeeRepository.upsertEmployee(employeeUiState.employeeDetails.toEmployee())
            }
        }
    }


    fun setEmployee(employee: Employee) {
        employeeUiState = employee.toEmployeeUiState()
        println("employee set in state: ${employee.firstName}")
    }

    fun onDaySelected(day: DayOfWeek, shiftType: ShiftType) {
        // Update the employeeDetails for the specific day
        val updatedEmployeeDetails = employeeUiState.employeeDetails.copy(
            monday = if (day == DayOfWeek.MONDAY) shiftType else employeeUiState.employeeDetails.monday,
            tuesday = if (day == DayOfWeek.TUESDAY) shiftType else employeeUiState.employeeDetails.tuesday,
            wednesday = if (day == DayOfWeek.WEDNESDAY) shiftType else employeeUiState.employeeDetails.wednesday,
            thursday = if (day == DayOfWeek.THURSDAY) shiftType else employeeUiState.employeeDetails.thursday,
            friday = if (day == DayOfWeek.FRIDAY) shiftType else employeeUiState.employeeDetails.friday,
            saturday = if (day == DayOfWeek.SATURDAY) shiftType else employeeUiState.employeeDetails.saturday,
            sunday = if (day == DayOfWeek.SUNDAY) shiftType else employeeUiState.employeeDetails.sunday
        )

        // Update the UI state with the updated employee details
        employeeUiState = employeeUiState.copy(employeeDetails = updatedEmployeeDetails)
    }


    // Function to validate nickname asynchronously
    suspend fun validateNickname(nickname: String): Boolean {
        // Use withContext to switch to the defaultDispatcher and return its result
        return withContext(defaultDispatcher) {
            val allEmployees = employeeRepository.getAllActiveEmployees().first()
            allEmployees.forEach { Log.d("nickname", it.nickname) }
            // Return the result of the validation check
            nickname.matches(alphaRegex) && allEmployees.none { it.nickname == nickname }
        }
    }
}


