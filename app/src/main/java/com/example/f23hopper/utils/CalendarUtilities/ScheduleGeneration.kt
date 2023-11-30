package com.example.f23hopper.utils.CalendarUtilities

import android.util.Log
import com.example.f23hopper.data.MutableInt
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.maxShiftsPerType
import java.time.LocalDate


data class ShiftAssignmentContext(
    val shiftsForDay: MutableList<Shift>,
    val assignedEmployeeIds: MutableSet<Long>,
    val shiftsNeededToBeAssigned: MutableInt,
    val shiftCounts: MutableMap<Long, Int>,
    val schedule: Map<LocalDate, List<Shift>>,
    val day: LocalDate,
    val shiftType: ShiftType
)

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
    val context = ShiftAssignmentContext(
        shiftsForDay,
        assignedEmployeeIds,
        remaining,
        shiftCounts,
        schedule,
        day,
        shiftType
    )

    Log.d("Generator", "Assigning ${remaining.value} $shiftType shifts for day $day")

    // assign openers and closers first.
    assignCertifiedEmployee(availableEmployees, context, Employee::canOpen, ShiftType.DAY)
    assignCertifiedEmployee(availableEmployees, context, Employee::canClose, ShiftType.NIGHT)

    assignRemainingShifts(availableEmployees, context)

    Log.d("Generator", "Assigned all required $shiftType shifts for day $day")
    return shiftsForDay
}

private fun assignCertifiedEmployee(
    availableEmployees: List<Employee>,
    context: ShiftAssignmentContext,
    isCertified: (Employee) -> Boolean,
    targetShiftType: ShiftType
) {
    if (context.shiftType == targetShiftType || context.shiftType == ShiftType.FULL) {
        // certified employees are all certified employees not already assigned to the given day,
        // sorted by shift count ascending
        val certifiedEmployees = availableEmployees
            .filter { isCertified(it) && !context.assignedEmployeeIds.contains(it.employeeId) }
            .sortedBy { context.shiftCounts.getOrDefault(it.employeeId, 0) }


        // if there are no certified employees scheduled already
        if (context.shiftsForDay.none { isCertified(it.employee) }) {
            // find the first eligible certified employee
            certifiedEmployees.firstOrNull()?.let { employee ->
                // if they're not already on the day already
                if (context.shiftsNeededToBeAssigned.value > 0 && !hasShiftOnDay(
                        employee,
                        context.day,
                        context.schedule,
                        targetShiftType
                    )
                ) {

                    // then add them in a new shift,
                    context.shiftsForDay.add(
                        createShift(employee, context.shiftType, context.day, context.shiftCounts)
                    )

                    // mark their id in the assigned employee list
                    context.assignedEmployeeIds.add(employee.employeeId)

                    // mark the shift as assigned
                    context.shiftsNeededToBeAssigned.value--
                }
            }
        }
    }
}

private fun assignRemainingShifts(
    availableEmployees: List<Employee>,
    context: ShiftAssignmentContext
) {
    val remainingEmployees = availableEmployees
        .filter { !context.assignedEmployeeIds.contains(it.employeeId) }
        .sortedBy { context.shiftCounts.getOrDefault(it.employeeId, 0) }

    for (employee in remainingEmployees) {
        // no more shifts need to be assigned
        if (context.shiftsNeededToBeAssigned.value == 0) break

        // skip if an employee reaches max shifts for the month/is already booked out for the day
        if (!canAssignMoreShifts(employee, context.shiftCounts) || hasShiftOnDay(
                employee,
                context.day,
                context.schedule,
                context.shiftType
            )
        ) {
            continue
        }
        // add the new shift,
        context.shiftsForDay.add(
            createShift(
                employee,
                context.shiftType,
                context.day,
                context.shiftCounts
            )
        )


        // add the id to the assigned employees set
        context.assignedEmployeeIds.add(employee.employeeId)

        // mark the shift as assigned
        context.shiftsNeededToBeAssigned.value--
    }
}

private fun hasShiftOnDay(
    employee: Employee,
    day: LocalDate,
    schedule: Map<LocalDate, List<Shift>>,
    shiftTypeToCheck: ShiftType
): Boolean {
    val shiftsForDay = schedule[day] ?: return false
    // Return true if the employee is working the same type of shift on the current day
    return shiftsForDay.any {
        it.employee.employeeId == employee.employeeId &&
                (it.schedule.shiftType == shiftTypeToCheck || it.schedule.shiftType == ShiftType.FULL)
    }
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
    val maxShiftsPerMonth = employee.maxShifts * 4// TEMP LOGIC UNTIL WE HAVE MAX COUNT
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

    // if schedule[day] is null, there are no schedules on this day
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
        result // result is implicitly returned as the bool for the all check
    }

    Log.d("ScheduleCheck", "Day $day is fully scheduled: $isFullyScheduled")
    return isFullyScheduled
}

