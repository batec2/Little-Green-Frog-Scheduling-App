package com.example.f23hopper.data

import EmployeeRepository
import android.content.Context

interface AppContainer {
    val employeeRepository: EmployeeRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val employeeRepository: EmployeeRepository by lazy{
        EmployeeRepository(EmployeesDatabase.getDatabase(context).employeeDao())
    }
}