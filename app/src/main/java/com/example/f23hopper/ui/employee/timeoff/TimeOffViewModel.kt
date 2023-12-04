package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.timeoff.TimeOff
import com.example.f23hopper.data.timeoff.TimeOffRepository
import com.example.f23hopper.ui.employee.EmployeeDetails
import com.example.f23hopper.ui.employee.EmployeeUiState
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class TimeOffViewModel @Inject constructor(
    private val timeOffRepository: TimeOffRepository,
    private val employeeRepository: EmployeeRepository,
    private val scheduleRepository: ScheduleRepository
):ViewModel(){
    var timeOffList by mutableStateOf(timeOffRepository.getAllTimeOff().asLiveData())
    var employeesList by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())
    var timeOffUiState by mutableStateOf(TimeOffUiState())
        private set


    fun addTimeOff() {
        checkIfValid(timeOffUiState)
        if(timeOffUiState.employee!=null&&timeOffUiState.start!=null&&timeOffUiState.end!=null){
            val timeOff =
                TimeOff(
                    employeeId = timeOffUiState.employee!!.employeeId,
                    dateFrom = formatDate(timeOffUiState.start!!),
                    dateTo = formatDate(timeOffUiState.end!!)
                )
            viewModelScope.launch {
                timeOffRepository.insert(timeOff)
            }
        }
    }

    fun checkIfValid(uiState:TimeOffUiState){
        uiState.isTimeOffValid = uiState.employee!=null&&uiState.start!=null&&uiState.end!=null
    }

    fun checkIfValid2(id:Long,start:java.util.Date,end:java.util.Date){
        val count = scheduleRepository.countOfShifts(id,start,end)
        //No shifts in the future
        //Start Date is 2 weeks in the future
    }
}

private fun formatDate(millis: Long): Date {
    return Date(millis).toJavaLocalDate().toSqlDate()
}

data class TimeOffUiState(
    var employee: Employee? = null,
    var start: Long? = null,
    var end: Long? = null,
    var isTimeOffValid: Boolean = true
)