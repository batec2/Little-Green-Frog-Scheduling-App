package com.example.f23hopper.ui.employee
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor (
    private val employeeRepository: EmployeeRepository
) : ViewModel() {
    var employees by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())
    var employeeUiState by mutableStateOf(EmployeeUiState())
        private set

    fun updateUiState(employeeDetails: EmployeeDetails) {
        employeeUiState =
            EmployeeUiState(
                employeeDetails = employeeDetails,
                isEmployeeValid = validateInput(employeeDetails)
            )
    }

    suspend fun deactivateEmployee(employee: Employee,value: Boolean){
        var active:Int = if(value) 0 else 1
        employeeRepository.deactivateEmployee(employee,active)
    }

    fun filterEmployee(filter: String){
        employees = when(filter) {
            "All Employees" -> employeeRepository.getAllActiveEmployees().asLiveData()
            "Can Open" -> employeeRepository.getCanOpen().asLiveData()
            "Can Close" -> employeeRepository.getCanClose().asLiveData()
            "Can Work Weekend" -> employeeRepository.getCanWorkWeekends().asLiveData()
            "Inactive" -> employeeRepository.getInactiveEmployees().asLiveData()
            else -> employeeRepository.getAllActiveEmployees().asLiveData()
        }
    }

    private fun validateInput(uiState: EmployeeDetails = employeeUiState.employeeDetails): Boolean {
        return with(uiState) {
            firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
                    && phoneNumber.isNotBlank()
        }
    }

    suspend fun saveEmployee() {
        if (validateInput()) { //checks if inputs are not blank
            employeeRepository.updateEmployee(employeeUiState.employeeDetails.toEmployee())
        }
    }

    fun setEmployee(employee: Employee){
        employeeUiState = employee.toEmployeeUiState()
        println("that: ${employee.firstName}")
    }
}
