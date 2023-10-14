package com.example.f23hopper.data.specialDay

import kotlinx.coroutines.flow.Flow
import java.sql.Date

class SpecialDayRepository(private val specialDayDao: SpecialDayDao) {
    suspend fun insert(specialDay: SpecialDay): Date {
        return specialDayDao.insert(specialDay)
    }

    suspend fun update(specialDay: SpecialDay) {
        specialDayDao.update(specialDay)
    }

    suspend fun delete(specialDay: SpecialDay) {
        specialDayDao.delete(specialDay)
    }

    fun getSpecialDays(): Flow<SpecialDay> {
        return specialDayDao.getSpecialDays()
    }

    suspend fun deleteAllSchedules() {
        return specialDayDao.deleteAllSpecialDays()
    }
}