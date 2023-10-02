package com.example.f23hopper.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.f23hopper.SchedulerApplication
import com.example.f23hopper.ui.employee.EmployeeEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            EmployeeEntryViewModel(schedulerApplication().container.employeeRepository)//This is equal to a day of work
        }
    }
}
//Instantiates the schedulerapplication object
fun CreationExtras.schedulerApplication(): SchedulerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SchedulerApplication)