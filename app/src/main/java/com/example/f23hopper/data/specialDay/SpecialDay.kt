package com.example.f23hopper.data.specialDay

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "specialdays")
data class SpecialDay(
    @PrimaryKey(autoGenerate = false)
    val date: Date
)
