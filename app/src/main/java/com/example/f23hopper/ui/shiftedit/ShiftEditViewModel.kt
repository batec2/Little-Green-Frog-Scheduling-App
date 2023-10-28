package com.example.f23hopper.ui.shiftedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import com.example.f23hopper.utils.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ShiftEditViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val specialDayRepo: SpecialDayRepository,
    private val employeeRepo: EmployeeRepository
) : ViewModel() {
    fun getShiftsForDay(date: LocalDate): Flow<List<Shift>> {
        return scheduleRepo.getShiftsByDate(date.toSqlDate())
    }

    suspend fun isSpecialDay(date: LocalDate): Boolean {
        return specialDayRepo.isDateInTable(date.toSqlDate())
    }


    private fun getWeekRangeForDate(date: java.time.LocalDate): Pair<java.time.LocalDate, java.time.LocalDate> {
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        return Pair(startOfWeek, endOfWeek)
    }

    fun getShiftCountsForWeek(dateInWeek: java.time.LocalDate): Flow<Map<Long, Int>> {
        val (startOfWeek, endOfWeek) = getWeekRangeForDate(dateInWeek)
        return scheduleRepo.getActiveShiftsByDateRange(
            startOfWeek.toSqlDate(),
            endOfWeek.toSqlDate()
        )
            .map { shifts ->
                shifts.groupBy { it.employee.employeeId }
                    .mapValues { (_, shifts) -> shifts.size }
            }
    }

    fun getEligibleEmployeesForShift(date: LocalDate, shiftType: ShiftType): Flow<List<Employee>> {
        return combine(
            employeeRepo.getEmployeesByDayAndShiftType(date.dayOfWeek, shiftType),
            getShiftsForDay(date)
        ) { allEligible, alreadyScheduled ->
            allEligible.filter { emp ->
                alreadyScheduled.none { shift ->
                    shift.employee.employeeId == emp.employeeId && shift.schedule.shiftTypeId == shiftType.ordinal
                }
            }
        }
    }


    fun addEmployeeToShift(employee: Employee, shiftType: ShiftType, date: LocalDate) {
        val newSchedule =
            Schedule(
                date = date.toSqlDate(),
                shiftTypeId = shiftType.ordinal,
                employeeId = employee.employeeId
            )
        viewModelScope.launch {
            scheduleRepo.insert(newSchedule)
        }
    }

    fun deleteShift(schedule: Schedule) {
        viewModelScope.launch {
            scheduleRepo.delete(schedule)
        }
    }


}

data class ShiftContext(
    val viewModel: ShiftEditViewModel,
    val date: LocalDate,
    val shiftsOnDay: Map<ShiftType, List<Shift>>,
    val isSpecialDay: Boolean
)