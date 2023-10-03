package com.example.f23hopper.ui.nav
enum class NavScreen(val route: String) {
    Calendar("Calendar"),
    EmployeeEntry("EmployeeEntry"),
    EmployeeList("EmployeeList");

    override fun toString(): String {
        return when (this) {
            Calendar -> "Calendar"
            EmployeeEntry -> "Employee Entry"
            EmployeeList -> "Employees"
        }
    }
}
