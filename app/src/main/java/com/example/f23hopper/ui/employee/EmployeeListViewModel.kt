package com.example.f23hopper.ui.employee
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.f23hopper.data.employee.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    val employees = employeeRepository.getAllEmployees().asLiveData()
}
