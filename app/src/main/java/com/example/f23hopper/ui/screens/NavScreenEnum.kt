package com.example.f23hopper.ui.screens
enum class NavScreen(val route: String) {
    EmployeeList("EmployeeList"),
    EmployeeEntry("EmployeeEntry"),
    Calendar("Calendar");

    override fun toString(): String {
        return when (this) {
            EmployeeList -> "Employee List"
            EmployeeEntry -> "Employee Entry"
            Calendar -> "Calendar"
        }
    }
}
