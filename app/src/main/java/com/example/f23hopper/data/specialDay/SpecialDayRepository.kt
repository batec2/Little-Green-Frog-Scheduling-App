package com.example.f23hopper.data.specialDay

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.sql.Date

class SpecialDayRepository(private val specialDayDao: SpecialDayDao) {
    @Insert
    suspend fun insert(date: Date): Date {
        return specialDayDao.insert(SpecialDay(date)).date
    }

    @Update
    suspend fun update(specialDay: SpecialDay) {
        specialDayDao.update(specialDay)
    }

    @Delete
    suspend fun delete(specialDay: SpecialDay) {
        specialDayDao.delete(specialDay)
    }

    fun getSpecialDays(): Flow<List<SpecialDay>> {
        return specialDayDao.getSpecialDays()
    }

    suspend fun deleteAllSchedules() {
        return specialDayDao.deleteAllSpecialDays()
    }
}