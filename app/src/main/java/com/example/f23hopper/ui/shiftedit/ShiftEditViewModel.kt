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
import com.example.f23hopper.utils.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
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


    fun getEligibleEmployeesForShift(date: LocalDate, shiftType: ShiftType): Flow<List<Employee>> {
        return combine(
            employeeRepo.getEmployeesByDayAndShiftType(date.dayOfWeek, shiftType),
            getShiftsForDay(date)
        ) { allEligible, alreadyScheduled ->
            allEligible.filter { emp ->
                alreadyScheduled.none { shift -> shift.employee.employeeId == emp.employeeId }
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
