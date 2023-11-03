package com.example.f23hopper.ui.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.ScheduleExporter
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val employeeRepository: EmployeeRepository,
    private val specialDayRepo: SpecialDayRepository
) : ViewModel() {
    //var employeeSchedule =
    //    mutableStateOf(scheduleRepo.getSchedulesForEmployee(null,null))

    // Exporting helper
    private val exporter: ScheduleExporter = ScheduleExporter()

    private val startDate: Date = getStartDate()
    private val endDate: Date = getEndDate()

    val shifts: StateFlow<List<Shift>> by lazy { parseShifts(fetchRawShifts()) }
    val employees: StateFlow<List<Employee>> by lazy { parseEmployees(fetchAllEmployees()) }
    val days: StateFlow<List<SpecialDay>> by lazy { parseSpecialDays(fetchRawSpecialDays()) }

    private fun getStartDate(): Date {
        val currentDate = LocalDate.now()
        return currentDate.minusMonths(2).withDayOfMonth(1).toSqlDate()
    }


    private fun getEndDate(): Date {
        val currentDate = LocalDate.now()
        return currentDate.plusMonths(12).withDayOfMonth(1).minusDays(1)
            .toSqlDate()
    }

    private fun fetchRawShifts(): Flow<List<Shift>> {
        return scheduleRepo.getActiveShiftsByDateRange(startDate, endDate)
    }

    private fun getShifts(): Flow<List<Shift>> {
        return scheduleRepo.getActiveShiftsByDateRange(startDate, endDate)
    }

    private fun parseShifts(rawShifts: Flow<List<Shift>>): StateFlow<List<Shift>> {
        return rawShifts
            .map { shifts -> shifts.sortedBy { it.schedule.shiftType } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun fetchRawSpecialDays(): Flow<List<SpecialDay>> {
        return specialDayRepo.getSpecialDays()
    }

    suspend fun toggleSpecialDay(date: Date?) {
        if (date != null) {
            specialDayRepo.toggleSpecialDay(date)
        }
    }

    private fun fetchAllEmployees(): Flow<List<Employee>> {
        return employeeRepository.getAllEmployees()
    }

    private fun parseEmployees(
        rawEmployees: Flow<List<Employee>>
    ): StateFlow<List<Employee>> {
        return rawEmployees
            .map { employees -> employees.sortedBy { it.lastName } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun parseSpecialDays(
        rawSpecialDays: Flow<List<SpecialDay>>
    ): StateFlow<List<SpecialDay>> {
        return rawSpecialDays
            .map { days -> days.sortedBy { it.date } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun exportSchedule(shifts: List<Shift>, curMonth: YearMonth, context: Context) {
        val content = exporter.formatFileData(shifts, curMonth)
        val filename = "${curMonth.year}-${
            curMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }_schedule.txt"
        val csvFile = exporter.createFile(content, context, filename = filename)
        exporter.shareFile(csvFile, context)


    }
}