package com.example.f23hopper.ui.calendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.ScheduleExporter
import com.example.f23hopper.utils.CalendarUtilities.assignShifts
import com.example.f23hopper.utils.CalendarUtilities.calculateRequiredShifts
import com.example.f23hopper.utils.CalendarUtilities.datesUntil
import com.example.f23hopper.utils.CalendarUtilities.isDayFullyScheduled
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val employeeRepository: EmployeeRepository,
    private val specialDayRepo: SpecialDayRepository,
) : ViewModel() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    private val startDate: Date
        get() {
            val currentDate = LocalDate.now()
            return currentDate.minusMonths(12).withDayOfMonth(1).toSqlDate()
        }

    private val endDate: Date
        get() {
            val currentDate = LocalDate.now()
            return currentDate.plusMonths(12).withDayOfMonth(1).minusDays(1).toSqlDate()
        }

    val shifts: StateFlow<List<Shift>> by lazy { parseShifts(fetchRawShifts()) }
    val employees: StateFlow<List<Employee>> by lazy { parseEmployees(fetchAllEmployees()) }
    val days: StateFlow<List<SpecialDay>> by lazy { parseSpecialDays(fetchRawSpecialDays()) }


    private fun fetchRawShifts(): Flow<List<Shift>> {
        return scheduleRepo.getAllShiftsByDateRange(startDate, endDate)
    }

    private fun parseShifts(rawShifts: Flow<List<Shift>>): StateFlow<List<Shift>> {
        return rawShifts.map { shifts -> shifts.sortedBy { it.schedule.shiftType } }
            .flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun fetchRawSpecialDays(): Flow<List<SpecialDay>> {
        return specialDayRepo.getSpecialDays()
    }

    fun toggleSpecialDay(date: Date?) {
        viewModelScope.launch {
            if (date != null) {
                // Launch the operation in the IO dispatcher
                withContext(defaultDispatcher) {
                    specialDayRepo.toggleSpecialDay(date)
                }
            }
        }
    }

    private fun fetchAllEmployees(): Flow<List<Employee>> {
        return employeeRepository.getAllEmployees()
    }

    private fun parseEmployees(
        rawEmployees: Flow<List<Employee>>
    ): StateFlow<List<Employee>> {
        return rawEmployees.map { employees -> employees.sortedBy { it.lastName } }
            .flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun parseSpecialDays(
        rawSpecialDays: Flow<List<SpecialDay>>
    ): StateFlow<List<SpecialDay>> {
        return rawSpecialDays.map { days -> days.sortedBy { it.date } }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun exportSchedule(
        shifts: List<Shift>,
        specialDays: List<SpecialDay>,
        curMonth: YearMonth,
        context: Context,
        onExportComplete: (String) -> Unit
    ) {
        // Exporting helper
        val exporter = ScheduleExporter(context, shifts, specialDays, curMonth)
        exporter.export()
        onExportComplete("Schedule saved to Downloads folder.")
    }


    private fun fetchShiftsForMonth(month: YearMonth): Flow<List<Shift>> {
        val startDate = month.atDay(1).toSqlDate()
        val endDate = month.atEndOfMonth().toSqlDate()
        return scheduleRepo.getActiveShiftsByDateRange(startDate, endDate)
    }

    private fun fetchSpecialDaysForMonth(month: YearMonth): Flow<List<SpecialDay>> {
        val startDate = month.atDay(1).toSqlDate()
        val endDate = month.atEndOfMonth().toSqlDate()
        return specialDayRepo.getSpecialDaysByDateRange(startDate, endDate)
    }

    //----Schedule Generation--------------------


    fun generateScheduleForMonth(month: YearMonth) {
        viewModelScope.launch {
            // fetch data to be used
            val preAssignedShifts = fetchShiftsForMonth(month).first()
            val specialDaysForMonth = fetchSpecialDaysForMonth(month).first()

            // get the count of shifts worked by each employee
            val shiftCounts = preAssignedShifts.groupBy { it.employee.employeeId }
                .mapValues { (_, v) -> v.count() }.toMutableMap()

            // map the existing shifts to a Map<LocalDate, List<Shifts>>
            val schedule = preAssignedShifts.groupBy { it.schedule.date.toJavaLocalDate() }
                .mapValues { (_, value) -> value.toMutableList() }
                .toMutableMap()

            // check each day in the month
            for (day in month.atDay(1).datesUntil(month.atEndOfMonth().plusDays(1))) {
                try {

                    // check if current day is a special day
                    val isSpecialDay = specialDaysForMonth.any { it.date.toJavaLocalDate() == day }

                    // skip days that are fully scheduled
                    if (isDayFullyScheduled(day, schedule, isSpecialDay)) continue


                    // get list of lists assigned already for the day
                    val assignedShifts = schedule[day]?.toMutableList() ?: mutableListOf()

                    // determine the number of required shifts for the day, mapped to type.
                    val requiredShiftsPerType =
                        calculateRequiredShifts(isSpecialDay, assignedShifts, day)

                    Log.d("Generator", "Starting shift assignment for day $day")
                    // for each shift type, assign shifts per each slot that still needs to  be filled
                    for ((shiftType, requiredCount) in requiredShiftsPerType) {

                        val alreadyAssignedCount =
                            assignedShifts.count { it.schedule.shiftType == shiftType }

                        Log.d(
                            "Generator",
                            "ShiftType: $shiftType, Required: $requiredCount, Already Assigned: $alreadyAssignedCount"
                        )

                        // fetch available employees for this shift type
                        val availableEmployees = employeeRepository.getEmployeesByDayAndShiftType(
                            day.dayOfWeek,
                            shiftType
                        ).first()

                        //  assign remaining shifts to employees prioritizing employees
                        //  that have less shifts
                        val newShiftsForDay = assignShifts(
                            availableEmployees,
                            requiredCount,
                            shiftType,
                            day,
                            shiftCounts,
                            schedule
                        )

                        //
                        assignedShifts.addAll(newShiftsForDay)
                    }


                    // update the schedule with the newly assigned shifts
                    schedule[day] = assignedShifts

                } catch (e: Exception) {
                    Log.e("Generator", "Error during generation for day $day", e)
                }
            }

            // update/insert the new schedule configuration
            upsertSchedules(schedule)
        }
    }

    private fun upsertSchedules(newSchedule: Map<LocalDate, List<Shift>>) {
        viewModelScope.launch(defaultDispatcher) {
            newSchedule.values.flatten().forEach { shift ->
                scheduleRepo.upsert(shift)
            }
        }
    }
}

