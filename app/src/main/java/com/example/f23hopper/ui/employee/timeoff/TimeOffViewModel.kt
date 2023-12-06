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
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.timeoff.TimeOff
import com.example.f23hopper.data.timeoff.TimeOffRepository
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class TimeOffViewModel @Inject constructor(
    private val timeOffRepository: TimeOffRepository,
    private val employeeRepository: EmployeeRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    var timeOffList by mutableStateOf(timeOffRepository.getAllTimeOff().asLiveData())
    var employeesList by mutableStateOf(employeeRepository.getAllActiveEmployees().asLiveData())
    var timeOffUiState by mutableStateOf(TimeOffUiState())
        private set

    //val employeesListNonState = employeeRepository.getAllEmployeesNonState()
    //--------------------------------------JUST COPIED FROM SHIFTS
    private val _activeShiftsInFuture = MutableStateFlow<List<Shift>>(emptyList())
    private val activeShiftsInFuture: StateFlow<List<Shift>> = _activeShiftsInFuture

    private val _timeOffInFuture = MutableStateFlow<List<TimeOff>>(emptyList())
    private val timeOffInFuture: StateFlow<List<TimeOff>> = _timeOffInFuture
    //--------------------------------------STILL NEEDS THIS^ BUT FOR TIME OFF LIST

    init {//--------------------------------------STILL NEEDS TIME OFF LIST
        viewModelScope.launch {
            scheduleRepository.getShiftsFromDate(java.time.LocalDate.now().toSqlDate()).collect {
                _activeShiftsInFuture.value = it
            }
            timeOffRepository.getTimeOffFromDate(java.time.LocalDate.now().toSqlDate()).collect {
                _timeOffInFuture.value = it
            }
        }
    }

    fun addTimeOff() {
        if (timeOffUiState.isTimeOffValid) {
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

    fun checkIfValid(): Boolean {
        if (timeOffUiState.employee != null && timeOffUiState.start != null && timeOffUiState.end != null) {
            val id = timeOffUiState.employee!!.employeeId
            val start = formatDate(timeOffUiState.start!!)
            val end = formatDate(timeOffUiState.end!!)
            val shifts =
                activeShiftsInFuture.value.filter { e -> e.employee.employeeId == id && e.schedule.date >= start && e.schedule.date <= end }
            println(shifts.isEmpty())

            val timeOff = timeOffInFuture.value.filter{e->e.id==id&&
                    (e.dateTo>=start&&e.dateFrom<=start)&&
                    (e.dateTo>=end&&e.dateFrom<=end)}

            timeOffUiState.isTimeOffValid = shifts.isEmpty()
            return shifts.isEmpty()
        }
        timeOffUiState.isTimeOffValid = false;
        return false
    }

    suspend fun deleteTimeOff(timeOff: TimeOff){
        timeOffRepository.delete(timeOff)
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