package com.example.f23hopper.data.timeoff

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "timeoff")
data class TimeOff (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val employeeId: Long,
    val dateFrom: Date,
    val dateTo: Date,
)