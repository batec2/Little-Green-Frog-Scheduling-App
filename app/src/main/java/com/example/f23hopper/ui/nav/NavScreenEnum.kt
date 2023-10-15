package com.example.f23hopper.ui.nav
enum class NavScreen(val route: String) {
    Calendar("Calendar"),
    CalendaWeekView("CalendarWeekView"),
    CalendarScheduleEntry("CalendarScheduleEntry"),
    EmployeeEntry("EmployeeEntry"),
    EmployeeEdit("EmployeeEdit"),
    SharedEmployeeList("SharedEmployeeList"),
    EmployeeList("EmployeeList");

    override fun toString(): String {
        return when (this) {
            Calendar -> "Calendar"
            CalendaWeekView->"CalendaWeekView"
            CalendarScheduleEntry->"CalendarScheduleEntry"
            EmployeeEntry -> "Employee Entry"
            EmployeeEdit -> "Employee Edit"
            SharedEmployeeList -> "Shared Employee List"
            EmployeeList -> "Employees"
        }
    }
}
