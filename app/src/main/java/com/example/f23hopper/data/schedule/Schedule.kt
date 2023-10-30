package com.example.f23hopper.data.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.shifttype.ShiftType
import java.sql.Date

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["employeeId"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val employeeId: Long,
    val shiftType: ShiftType
)
