package com.example.f23hopper.ui.employee
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor (
    private val employeeRepository: EmployeeRepository
) : ViewModel() {
    val employees = employeeRepository.getAllEmployees().asLiveData()
    var employeeUiState by mutableStateOf(EmployeeUiState())
        private set

    fun setEmployee(employee: Employee){
        employeeUiState = employee.toEmployeeUiState()
        println("that: ${employee.firstName}")
    }
}
