package com.example.f23hopper.data.employee

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.f23hopper.data.shifttype.ShiftType

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    var employeeId: Long = 0,
    var active: Boolean,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    var canOpen: Boolean,
    var canClose: Boolean,
    var sunday: ShiftType,
    var monday: ShiftType,
    var tuesday: ShiftType,
    var wednesday: ShiftType,
    var thursday: ShiftType,
    var friday: ShiftType,
    var saturday: ShiftType,
)
