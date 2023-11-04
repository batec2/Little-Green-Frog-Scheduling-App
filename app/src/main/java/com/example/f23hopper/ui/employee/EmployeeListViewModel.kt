package com.example.f23hopper.ui.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.DayOfWeek
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.shifttype.ShiftType
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

    fun resetUiState() {
        employeeUiState =
            EmployeeUiState(
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
            if (validateInput(employeeUiState.employeeDetails)) {
                // If the nickname is blank, generate a new one
                val nickname = employeeUiState.employeeDetails.nickname.ifBlank {
                    generateUniqueNickname()
                }

                // Create a copy of employeeDetails with the new nickname
                val employeeDetailsWithNickname =
                    employeeUiState.employeeDetails.copy(nickname = nickname)

                // Insert the employee with the new nickname
                employeeRepository.upsertEmployee(employeeDetailsWithNickname.toEmployee())
            }
        }
    }


    private suspend fun generateUniqueNickname(): String {
        var nickname: String
        do {
            // Generate a random nickname
            nickname = generateNickname()

            // Check if any employee already has this nickname
            val isNicknameUsed = employeeRepository.isNicknameUsed(nickname)
        } while (isNicknameUsed)

        return nickname
    }


    private fun generateNickname(): String {
        return listOf(
            "Sparky", "Ace", "Shadow", "Gizmo", "Maverick", "Rogue", "Zeus", "Bandit",
            "Bolt", "Chief", "Dash", "Echo", "Falcon", "Gadget", "Hawk", "Iceman",
            "Jester", "Krypto", "Lynx", "Mystic", "Nebula", "Orion", "Phantom", "Quicksilver",
            "Racer", "Saber", "Titan", "Ulysses", "Vortex", "Wizard", "Xenon", "Yoda",
            "Zephyr", "Blaze", "Cosmo", "Drift", "Eclipse", "Flame", "Glitch", "Hurricane",
            "Inferno", "Jolt", "Knight", "Laser", "Mirage", "Nova", "Omega", "Pulse", "Quantum",
            "Rift"
        ).random()
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

}
