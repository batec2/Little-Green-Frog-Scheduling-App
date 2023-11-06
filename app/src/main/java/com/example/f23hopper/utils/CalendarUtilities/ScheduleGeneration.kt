package com.example.f23hopper.utils.CalendarUtilities

import android.util.Log
import com.example.f23hopper.data.MutableInt
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.maxShiftsPerType
import java.time.LocalDate

fun assignShifts(
    availableEmployees: List<Employee>,
    remainingCount: Int,
    shiftType: ShiftType,
    day: LocalDate,
    shiftCounts: MutableMap<Long, Int>,
    schedule: Map<LocalDate, List<Shift>>
): List<Shift> {
    val shiftsForDay = mutableListOf<Shift>()
    val remaining = MutableInt(remainingCount)
    val assignedEmployeeIds = mutableSetOf<Long>()

    Log.d("Generator", "Assigning ${remaining.value} $shiftType shifts for day $day")

    fun filterAndSortEmployees(predicate: (Employee) -> Boolean): List<Employee> =
        availableEmployees.filter(predicate)
            .sortedBy { shiftCounts.getOrDefault(it.employeeId, 0) }

    // assign opener and closer
    if (shiftType == ShiftType.DAY || shiftType == ShiftType.FULL) {
        val openers =
            filterAndSortEmployees { it.canOpen && !assignedEmployeeIds.contains(it.employeeId) }
        assignIfPossible(
            openers,
            assignedEmployeeIds,
            shiftsForDay,
            remaining,
            shiftType,
            day,
            shiftCounts,
            schedule
        )
    }
    if (shiftType == ShiftType.NIGHT || shiftType == ShiftType.FULL) {
        val closers =
            filterAndSortEmployees { it.canClose && !assignedEmployeeIds.contains(it.employeeId) }
        assignIfPossible(
            closers,
            assignedEmployeeIds,
            shiftsForDay,
            remaining,
            shiftType,
            day,
            shiftCounts,
            schedule
        )
    }

    // Assign the remaining shifts
    val remainingEmployees = filterAndSortEmployees { !assignedEmployeeIds.contains(it.employeeId) }
    remainingEmployees.forEach { employee ->
        if (remaining.value == 0 || !canAssignMoreShifts(employee, shiftCounts) || hasShiftOnDay(
                employee,
                day,
                schedule
            )
        ) {
            Log.d(
                "Generator",
                "No more shifts needed or Employee ${employee.employeeId} cannot be assigned more shifts or already has a shift on day $day"
            )
            return@forEach // continue
        }
        shiftsForDay.add(createShift(employee, shiftType, day, shiftCounts))
        assignedEmployeeIds.add(employee.employeeId)
        remaining.value--
    }

    Log.d("Generator", "Assigned all required $shiftType shifts for day $day")
    return shiftsForDay
}

private fun assignIfPossible(
    employees: List<Employee>,
    assignedEmployeeIds: MutableSet<Long>,
    shiftsForDay: MutableList<Shift>,
    remaining: MutableInt, // using custom class so it passes by reference
    shiftType: ShiftType,
    day: LocalDate,
    shiftCounts: MutableMap<Long, Int>,
    schedule: Map<LocalDate, List<Shift>>
) {
    employees.firstOrNull()?.let { employee ->
        if (!hasShiftOnDay(employee, day, schedule)) {
            shiftsForDay.add(createShift(employee, shiftType, day, shiftCounts))
            assignedEmployeeIds.add(employee.employeeId)
            remaining.value--
        }
    }
}

private fun hasShiftOnDay(
    employee: Employee,
    day: LocalDate,
    schedule: Map<LocalDate, List<Shift>>
): Boolean {
    val shiftsForDay = schedule[day] ?: return false
    return shiftsForDay.any { it.employee.employeeId == employee.employeeId }
}

private fun createShift(
    employee: Employee,
    shiftType: ShiftType,
    day: LocalDate,
    shiftCounts: MutableMap<Long, Int>
): Shift {
    val newShift = Shift(
        schedule = Schedule(
            date = day.toSqlDate(),
            employeeId = employee.employeeId,
            shiftType = shiftType
        ), employee = employee
    )
    Log.d(
        "Generator",
        "Assigned $shiftType shift to employee ${employee.employeeId} on day $day"
    )

    // update the shift count for the employee
    shiftCounts[employee.employeeId] = shiftCounts.getOrDefault(employee.employeeId, 0) + 1
    return newShift
}


private fun canAssignMoreShifts(
    employee: Employee, shiftCounts: MutableMap<Long, Int>
): Boolean {
    // future logic to determine max shifts per month for an employee
    val currentCount = shiftCounts.getOrDefault(employee.employeeId, 0)
    //TODO implement max count in employee and use it here
    // val maxShiftsPerMonth = employee.maxCount // or something
    val maxShiftsPerMonth = 1000 // TEMP LOGIC UNTIL WE HAVE MAX COUNT
    return currentCount < maxShiftsPerMonth
}

fun calculateRequiredShifts(
    isSpecialDay: Boolean, assignedShifts: List<Shift>, day: LocalDate
): Map<ShiftType, Int> {

    // get shift counts then subtract by shifts already filled
    // this gives the amt of spots to be filled
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


fun isDayFullyScheduled(
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

