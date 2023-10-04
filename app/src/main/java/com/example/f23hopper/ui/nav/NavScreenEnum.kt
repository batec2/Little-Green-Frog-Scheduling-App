package com.example.f23hopper.ui.nav
enum class NavScreen(val route: String) {
    Calendar("Calendar"),
    CalendarDayView("CalendarDayView"),
    CalendarScheduleEntry("CalendarScheduleEntry"),
    EmployeeEntry("EmployeeEntry"),
    EmployeeList("EmployeeList");

    override fun toString(): String {
        return when (this) {
            Calendar -> "Calendar"
            CalendarDayView->"CalendarDayView"
            CalendarScheduleEntry->"CalendarScheduleEntry"
            EmployeeEntry -> "Employee Entry"
            EmployeeList -> "Employees"
        }
    }
}
