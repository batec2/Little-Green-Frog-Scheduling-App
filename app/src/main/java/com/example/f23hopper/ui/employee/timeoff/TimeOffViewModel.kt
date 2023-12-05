package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.timeoff.TimeOff
import com.example.f23hopper.data.timeoff.TimeOffRepository
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
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
        if(checkIfValid()){
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

    fun checkIfValid():Boolean{
        var countShifts = 1
        var countTimeOff = 1
        if(timeOffUiState.employee!=null&&timeOffUiState.start!=null&&timeOffUiState.end!=null){
            countShifts = scheduleRepository
                .countOfShifts(
                    timeOffUiState.employee!!.employeeId,
                    formatDate(timeOffUiState.start!!),
                    formatDate(timeOffUiState.end!!)
                )
            countTimeOff = timeOffRepository
                .countOfTimeOff(
                    timeOffUiState.employee!!.employeeId,
                    formatDate(timeOffUiState.start!!),
                    formatDate(timeOffUiState.end!!)
                )
        }
        return countShifts==0&&countTimeOff==0
    }

}

private fun formatDate(millis: Long): Date {
    return Date(millis).toJavaLocalDate().toSqlDate()
}

data class TimeOffUiState(
    var employee: Employee? = null,
    var start: Long? = null,
    var end: Long? = null,
    var isTimeOffValid: Boolean = false
)