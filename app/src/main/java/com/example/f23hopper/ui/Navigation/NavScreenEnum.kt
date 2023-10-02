package com.example.f23hopper.ui.Navigation
enum class NavScreen(val route: String) {
    EmployeeList("EmployeeList"),
    EmployeeEntry("EmployeeEntry"),
    Calendar("Calendar");

    override fun toString(): String {
        return when (this) {
            EmployeeList -> "Employees"
            EmployeeEntry -> "Employee Entry"
            Calendar -> "Calendar"
        }
    }
}
