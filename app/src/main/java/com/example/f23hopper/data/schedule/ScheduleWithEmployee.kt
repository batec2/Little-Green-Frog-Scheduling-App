package com.example.f23hopper.data.schedule

import androidx.room.Embedded
import androidx.room.Relation
import com.example.f23hopper.data.employee.Employee

data class ScheduleWithEmployee(
    @Embedded val schedule: Schedule,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "employeeId"
    )
    val employee: Employee
)
