package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.toKotlinxLocalDate
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
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val employeeRepository: EmployeeRepository,
    private val specialDayRepo: SpecialDayRepository
) : ViewModel() {


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

    private fun parseShifts(rawShifts: Flow<List<Shift>>): StateFlow<List<Shift>> {
        return rawShifts
            .map { shifts -> shifts.sortedBy { it.schedule.shiftType } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun fetchRawSpecialDays(): Flow<List<SpecialDay>> {
        return specialDayRepo.getSpecialDays()
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

    fun formatShiftsToCsv(shifts: List<Shift>, curMonth: YearMonth): String {
        val header = "Date,Shift Type,Employee Name\n"
        val filteredShifts = shifts.filter { shift ->
            val scheduleDate = shift.schedule.date.toKotlinxLocalDate()
            scheduleDate.year == curMonth.year && scheduleDate.month == curMonth.month
        }
        val rows = filteredShifts.groupBy { it.schedule.date }
            .toList()
            .sortedBy { it.first }
            .joinToString("\n") { (_, groupedShifts) ->
                groupedShifts.joinToString("\n") { shift ->
                    "${shift.schedule.date},${shift.schedule.shiftType},${shift.employee.firstName} ${shift.employee.lastName}"
                }
            }
        return header + rows
    }

}