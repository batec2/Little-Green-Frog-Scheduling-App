package com.example.f23hopper.ui.calendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.employee.availableDays
import com.example.f23hopper.data.employee.shiftTypeForDay
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.ScheduleExporter
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import com.example.f23hopper.utils.maxShifts
import dagger.hilt.android.lifecycle.HiltViewModel
import getEmployeesWithNoShifts
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val employeeRepository: EmployeeRepository,
    private val specialDayRepo: SpecialDayRepository,
) : ViewModel() {
    // Exporting helper
    private val exporter: ScheduleExporter = ScheduleExporter()

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
        return scheduleRepo.getActiveShiftsByDateRange(startDate, endDate)
    }

    private fun getShifts(): Flow<List<Shift>> {
        return scheduleRepo.getActiveShiftsByDateRange(startDate, endDate)
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

    private fun fetchAllActiveEmployees(): Flow<List<Employee>> {
        return employeeRepository.getAllActiveEmployees()
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
        val content = exporter.formatData(shifts, curMonth)
        val filename = "${curMonth.year}_${curMonth.month.value}_schedule"
        exporter.export(filename, content, context, shifts, specialDays, curMonth)
        onExportComplete("$filename saved to Downloads folder")
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

    private fun isDayFullyScheduled(
        day: LocalDate, schedule: Map<LocalDate, List<Shift>>, isSpecialDay: Boolean
    ): Boolean {
        Log.d("ScheduleCheck", "Checking if day $day is fully scheduled.")
        val assignedShifts = schedule[day] ?: run {
            Log.d("ScheduleCheck", "Day $day has no assigned shifts.")
            return false
        }

        // get the required number of shifts for each shift type
        val requiredShifts = maxShiftsPerType(day, isSpecialDay)

        // count the number of assigned shifts for each type
        val assignedShiftCounts = assignedShifts.groupingBy { it.schedule.shiftType }.eachCount()

        // log assigned shift counts
        Log.d("ScheduleCheck", "Assigned shift counts for day $day: $assignedShiftCounts")

        // check if all required shifts are assigned
        val isFullyScheduled = requiredShifts.all { (shiftType, requiredCount) ->
            val assignedCount = assignedShiftCounts[shiftType] ?: 0
            val result = assignedCount >= requiredCount
            Log.d(
                "ScheduleCheck",
                "Day $day has $assignedCount/$requiredCount assigned for $shiftType. Fully scheduled: $result"
            )
            result
        }

        Log.d("ScheduleCheck", "Day $day is fully scheduled: $isFullyScheduled")
        return isFullyScheduled
    }

    fun generateScheduleForMonth(month: YearMonth) {
        viewModelScope.launch {
            Log.d("Generator", "Starting Generation")
            // fetch data to be used
            val preAssignedShifts = fetchShiftsForMonth(month).first()
            val allEmployees = fetchAllEmployees().first()
            val specialDaysForMonth = fetchSpecialDaysForMonth(month).first()

            // shifts by employee list
            val shiftCounts = preAssignedShifts.groupBy { it.employee.employeeId }
                .mapValues { (_, v) -> v.count() }.toMutableMap()

            // get the schedule with all of the existing shifts
            val schedule = preAssignedShifts.groupBy { it.schedule.date.toJavaLocalDate() }
                .mapValues { (_, value) -> value.toMutableList() }
                .toMutableMap()

            Log.d("Generator", "Built PreSchedule")
            val employeesNeedingShifts =
                getEmployeesWithNoShifts(preAssignedShifts, month, allEmployees)

            // check each day in the month
            for (day in month.atDay(1).datesUntil(month.atEndOfMonth().plusDays(1))) {
                try {

                    // skip days that are fully scheduled
                    val isSpecialDay = specialDaysForMonth.any { it.date.toJavaLocalDate() == day }
                    if (isDayFullyScheduled(day, schedule, isSpecialDay)) continue


                    // determine the number of required shifts based on the day type
                    val assignedShiftsForDay = schedule[day].orEmpty()
                    val requiredShiftsPerType =
                        calculateRequiredShifts(isSpecialDay, assignedShiftsForDay, day)

                    // assign shifts to employees while taking into account the already assigned shifts
                    val assignedShifts = schedule[day]?.toMutableList() ?: mutableListOf()
                    Log.d("Generator", "Starting shift assignment for day $day")
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

                        //  assign remaining shifts to employees prioritizing emps that have less shifts
                        val shiftsForDay = assignShifts(
                            availableEmployees,
                            requiredCount,
                            shiftType,
                            day,
                            shiftCounts,
                        )

                        assignedShifts.addAll(shiftsForDay)
                    }

                    // check if there are employees needing shifts and perform swaps if necessary
//                    val employeesNeedingShiftsForDay = employeesNeedingShifts[day] ?: listOf()
//                    if (employeesNeedingShiftsForDay.isNotEmpty()) {
//                        performShiftSwaps(schedule, shiftCounts, employeesNeedingShiftsForDay, day)
//                    }

                    // update the schedule with the newly assigned shifts
                    schedule[day] = assignedShifts
                } catch (e: Exception) {
                    Log.e("Generator", "Error during generation for day $day", e)
                } finally {
                    Log.d("Generator", "Coroutine is completing or being canceled.")
                }
            }
            Log.d("Generator", "Finished day scan")

            insertNewSchedules(schedule)
            Log.d("Generator", "Finished Generation")
        }
    }

    // Currently bugged, discrepency when adding FULL shifts.
    private fun performShiftSwaps(
        schedule: MutableMap<LocalDate, MutableList<Shift>>,
        shiftCounts: MutableMap<Long, Int>,
        employeesNeedingShifts: List<Employee>,
        day: LocalDate
    ) {
        Log.d("ShiftSwap", "Starting shift swaps for day: $day")

        // iterate through employees who need shifts
        for (employeeNeedingShift in employeesNeedingShifts) {
            Log.d(
                "ShiftSwap",
                "Looking for shifts for employee: ${employeeNeedingShift.employeeId}"
            )

            // determine the shift type needed for the employeeNeedingShift on the day
            val neededShiftType = employeeNeedingShift.shiftTypeForDay(day.dayOfWeek)
            Log.d(
                "ShiftSwap",
                "Needed shift type for employee ${employeeNeedingShift.employeeId} on day $day is $neededShiftType"
            )

            // find a suitable employee to swap with
            for ((scheduledDay, shifts) in schedule) {
                // skip the current day as we're looking for a swap
                if (scheduledDay == day) continue

                Log.d("ShiftSwap", "Looking at scheduled day: $scheduledDay for possible swaps")

                // look for an employee who has a shift on a different day and can work on the current day with the correct shift type
                val employeeToSwap = shifts.find { shift ->
                    val canEmployeeNeedingShiftWork =
                        employeeNeedingShift.shiftTypeForDay(day.dayOfWeek) == shift.schedule.shiftType
                    val canEmployeeToSwapWork =
                        shift.employee.shiftTypeForDay(day.dayOfWeek) == neededShiftType

                    shift.employee.availableDays().contains(day.dayOfWeek) &&
                            employeeNeedingShift.availableDays().contains(scheduledDay.dayOfWeek) &&
                            canEmployeeNeedingShiftWork && canEmployeeToSwapWork
                }?.employee

                // if a suitable employee is found, perform the swap
                if (employeeToSwap != null) {
                    Log.d(
                        "ShiftSwap",
                        "Found employee to swap. Employee ID: ${employeeToSwap.employeeId}"
                    )

                    // log the shift type before the swap
                    val shiftToSwap = shifts.first { it.employee == employeeToSwap }
                    Log.d("ShiftSwap", "Shift type before swap: ${shiftToSwap.schedule.shiftType}")

                    // remove the shift from the original day
                    shifts.removeAt(shifts.indexOfFirst { it.employee == employeeToSwap })

                    // update the shift to the new day and employee
                    val newShift = shiftToSwap.copy(
                        schedule = shiftToSwap.schedule.copy(
                            date = day.toSqlDate(),
                            employeeId = employeeNeedingShift.employeeId,
                            // set correctly?
                            shiftType = employeeNeedingShift.shiftTypeForDay(day.dayOfWeek)
                        ),
                        employee = employeeNeedingShift
                    )

                    Log.d("ShiftSwap", "Shift type after swap: ${newShift.schedule.shiftType}")

                    // add the new shift to the current day
                    schedule[day]?.add(newShift)

                    Log.d(
                        "ShiftSwap",
                        "Swapped shift of employee ${employeeToSwap.employeeId} to employee ${employeeNeedingShift.employeeId} for day $day"
                    )

                    // update the shift count for both employees
                    shiftCounts[employeeToSwap.employeeId] =
                        shiftCounts.getOrDefault(employeeToSwap.employeeId, 0) - 1
                    shiftCounts[employeeNeedingShift.employeeId] =
                        shiftCounts.getOrDefault(employeeNeedingShift.employeeId, 0) + 1

                    // exit the inner loop as the swap is complete
                    break
                } else {
                    Log.d(
                        "ShiftSwap",
                        "No suitable employee found to swap with for employee ${employeeNeedingShift.employeeId} on day $scheduledDay"
                    )
                }
            }
        }

        Log.d("ShiftSwap", "Completed shift swaps for day: $day")
    }

    private fun insertNewSchedules(newSchedule: Map<LocalDate, List<Shift>>) {
        viewModelScope.launch(defaultDispatcher) {
            newSchedule.values.flatten().forEach { shift ->
                scheduleRepo.upsert(shift)
            }
        }
    }

    private fun assignShifts(
        availableEmployees: List<Employee>,
        remainingCount: Int,
        shiftType: ShiftType,
        day: LocalDate,
        shiftCounts: MutableMap<Long, Int>
    ): List<Shift> {
        val shiftsForDay = mutableListOf<Shift>()
        var remaining = remainingCount

        Log.d("Generator", "Assigning $remaining $shiftType shifts for day $day")

        // sort employees by the number of shifts they've been assigned, ascending
        val sortedEmployees =
            availableEmployees.sortedBy { shiftCounts.getOrDefault(it.employeeId, 0) }

        for (employee in sortedEmployees) {
            // immediately check if we have assigned enough shifts
            if (remaining == 0) {
                Log.d("Generator", "Assigned all required $shiftType shifts for day $day")
                break
            }

            // Check if the employee can be assigned more shifts
            if (!canAssignMoreShifts(employee, shiftCounts)) {
                Log.d("Generator", "Employee ${employee.employeeId} cannot be assigned more shifts")
                continue
            }

            // create a new shift and add to the shifts for the day
            shiftsForDay.add(
                Shift(
                    schedule = Schedule(
                        date = day.toSqlDate(),
                        employeeId = employee.employeeId,
                        shiftType = shiftType
                    ), employee = employee
                )
            )

            Log.d(
                "Generator",
                "Assigned $shiftType shift to employee ${employee.employeeId} on day $day"
            )

            // update the shift count for the employee
            shiftCounts[employee.employeeId] = shiftCounts.getOrDefault(employee.employeeId, 0) + 1

            remaining--
        }

        return shiftsForDay
    }


    private fun canAssignMoreShifts(
        employee: Employee, shiftCounts: MutableMap<Long, Int>
    ): Boolean {
        // future logic to determine max shifts per month for an employee
        val currentCount = shiftCounts.getOrDefault(employee.employeeId, 0)
        val maxShiftsPerMonth = 1000 // TEMP LOGIC UNTIL WE HAVE MAX COUNT
        return currentCount < maxShiftsPerMonth
    }

    private fun calculateRequiredShifts(
        isSpecialDay: Boolean, assignedShifts: List<Shift>, day: LocalDate
    ): Map<ShiftType, Int> {
        Log.d(
            "Generator",
            "Calculating required shifts for ${if (isSpecialDay) "special" else "regular"} day"
        )
        val baseRequirements = maxShiftsPerType(day, isSpecialDay)

        // calculate the remaining required shifts for each type
        val remainingRequirements = baseRequirements.toMutableMap()
        Log.d("Generator", "Base requirements: $baseRequirements")

        assignedShifts.forEach { shift ->
            remainingRequirements[shift.schedule.shiftType]?.let {
                val updatedCount = it - 1
                remainingRequirements[shift.schedule.shiftType] = updatedCount
                Log.d(
                    "Generator",
                    "Reduced requirement for ${shift.schedule.shiftType}: $updatedCount"
                )
            }
        }

        // remove shift types that are fully booked
        val finalRequirements = remainingRequirements.filter { it.value > 0 }
        Log.d("Generator", "Final required shifts: $finalRequirements")
        return finalRequirements
    }
}

private fun maxShiftsPerType(day: LocalDate, isSpecialDay: Boolean): Map<ShiftType, Int> {
    val isWeekend = day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY
    val shiftCountPerDay = maxShifts(isSpecialDay)

    return if (isWeekend) {
        mapOf(
            ShiftType.FULL to shiftCountPerDay
        )
    } else {
        mapOf(
            ShiftType.DAY to shiftCountPerDay,
            ShiftType.NIGHT to shiftCountPerDay
        )
    }
}

private fun LocalDate.datesUntil(endExclusive: LocalDate): Sequence<LocalDate> {
    return generateSequence(this) { currentDate ->
        currentDate.plusDays(1).takeIf { it.isBefore(endExclusive) }
    }
}

