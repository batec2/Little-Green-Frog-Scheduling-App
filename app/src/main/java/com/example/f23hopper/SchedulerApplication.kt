package com.example.f23hopper

import EmployeeRepository
import android.app.Application
import com.example.f23hopper.data.AppContainer
import com.example.f23hopper.data.AppDataContainer
import com.example.f23hopper.data.EmployeesDatabase

class SchedulerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}