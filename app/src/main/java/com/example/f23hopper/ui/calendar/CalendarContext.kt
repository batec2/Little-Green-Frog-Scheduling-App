package com.example.f23hopper.ui.calendar

import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.kizitonwose.calendar.core.CalendarDay

data class CalendarContext(
    val shifts: List<Shift>,
    val employees: List<Employee>,
    val specialDays: List<SpecialDay>,
    val navigateToShiftView: (String) -> Unit,
    val viewModel: CalendarViewModel,
    val selection: CalendarDay?,
    val onSelectionChanged: (CalendarDay?) -> Unit
)
