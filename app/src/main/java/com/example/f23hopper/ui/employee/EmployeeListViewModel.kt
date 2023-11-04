package com.example.f23hopper.ui.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    var employees by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())

    var showConfirmationDialog by mutableStateOf(false)
        private set

    var employeeToToggle: Employee? by mutableStateOf(null)
        private set

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
        employeeUiState =
            EmployeeUiState(
                employee = employeeUiState.employee,
                employeeDetails = employeeDetails,
                isEmployeeValid = validateInput(employeeDetails)
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
            if (validateInput(employeeUiState.employeeDetails)) { //checks if inputs are not blank
                employeeRepository.updateEmployee(employeeUiState.employeeDetails.toEmployee())
            }
        }
    }

    fun setEmployee(employee: Employee) {
        employeeUiState = employee.toEmployeeUiState()
        println("employee set in state: ${employee.firstName}")
    }
}
