package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.timeoff.TimeOffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimeOffViewModel @Inject constructor(
    timeOffRepository: TimeOffRepository,
    employeeRepository: EmployeeRepository
):ViewModel(){
    //private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    var timeOffList by mutableStateOf(timeOffRepository.getAllTimeOff().asLiveData())
    var employeesList by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())
}