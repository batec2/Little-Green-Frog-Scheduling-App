package com.example.f23hopper.data.timeoff

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.sql.Date

class TimeOffRepository(private val timeOffDao: TimeOffDao) {
    @Insert
    suspend fun insert(timeOff:TimeOff) {
        timeOffDao.insert(timeOff)
    }

    @Update
    suspend fun update(timeOff: TimeOff) {
        timeOffDao.update(timeOff)
    }

    @Delete
    suspend fun delete(timeOff: TimeOff) {
        timeOffDao.delete(timeOff)
    }

    fun getAllTimeOff(): Flow<List<TimeOff>> {
        return timeOffDao.getTimeOff()
    }

}