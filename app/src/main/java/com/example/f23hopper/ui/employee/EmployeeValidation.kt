package com.example.f23hopper.ui.employee

import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType


fun hasCriticalShifts(employee: Employee, allShifts: List<Shift>): Boolean {
    val shiftsWithEmployee = allShifts.filter { it.employee == employee }

    return shiftsWithEmployee.any { shift ->
        isCriticalShift(employee, shift, allShifts)
    }
}

private fun isCriticalShift(employee: Employee, shift: Shift, allShifts: List<Shift>): Boolean {
    val shiftsOnSameDay = allShifts.filter { it.schedule.date == shift.schedule.date }
    val otherEmployeesOnSameDay = shiftsOnSameDay.filter { it.employee != employee }

    return when (shift.schedule.shiftType) {
        ShiftType.FULL -> isOnlyOpenerOrCloser(employee, otherEmployeesOnSameDay)
        ShiftType.DAY -> isOnlyOpener(employee, otherEmployeesOnSameDay)
        ShiftType.NIGHT -> isOnlyCloser(employee, otherEmployeesOnSameDay)
        else -> false
    }
}

private fun isOnlyOpenerOrCloser(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return isOnlyOpener(employee, otherEmployees) || isOnlyCloser(employee, otherEmployees)
}

private fun isOnlyOpener(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return employee.canOpen && otherEmployees.none { it.employee.canOpen }
}

private fun isOnlyCloser(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return employee.canClose && otherEmployees.none { it.employee.canClose }
}
