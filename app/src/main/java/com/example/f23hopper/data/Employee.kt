package com.example.f23hopper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val employeeId: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    var canOpen: Boolean,
    var canClose: Boolean,
    var sunday: Int,
    var monday: Int,
    var tuesday: Int,
    var wednesday: Int,
    var thursday: Int,
    var friday: Int,
    var saturday: Int
)
