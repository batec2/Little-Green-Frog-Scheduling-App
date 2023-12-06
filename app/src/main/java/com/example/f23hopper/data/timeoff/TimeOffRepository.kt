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

    fun getAllTimeOffNonState(): List<TimeOff> {
        return timeOffDao.getTimeOffNonState()
    }

    fun getTimeOffByDate(date: Date): Flow<List<TimeOff>> {
        return timeOffDao.getTimeOffByDate(date)
    }

    fun countOfTimeOff(employeeId: Long, startDate: Date, endDate: Date): Int {
        return timeOffDao.countOfTimeOff(employeeId, startDate, endDate)
    }
}