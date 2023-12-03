package com.example.f23hopper.ui.nav
enum class NavScreen(val route: String) {
    Calendar("Calendar"),
    ScheduleEdit("ScheduleEdit"),
    CalendarScheduleEntry("CalendarScheduleEntry"),
    EmployeeEntry("EmployeeEntry"),
    EmployeeEdit("EmployeeEdit"),
    SharedEmployeeList("SharedEmployeeList"),
    EmployeeList("EmployeeList"),
    EmployeeTimeOff("EmployeeTimeOff");

    override fun toString(): String {
        return when (this) {
            Calendar -> "Calendar"
            ScheduleEdit -> "ScheduleEdit"
            CalendarScheduleEntry -> "CalendarScheduleEntry"
            EmployeeEntry -> "Employee Entry"
            EmployeeEdit -> "Employee Edit"
            SharedEmployeeList -> "Shared Employee List"
            EmployeeList -> "Employees"
            EmployeeTimeOff -> "EmployeeTimeOff"
        }
    }
}
