package com.example.f23hopper.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    val id: Int = 0,
    val date: Date,
    val employeeId: Int,
    val shiftTypeId: Int
)
