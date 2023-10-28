package com.example.f23hopper.data.schedule

import kotlinx.coroutines.flow.Flow
import java.sql.Date

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    suspend fun insert(schedule: Schedule): Long {
        return scheduleDao.insert(schedule)
    }

    suspend fun update(schedule: Schedule) {
        scheduleDao.update(schedule)
    }

    suspend fun delete(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }

    fun getScheduleById(id: Int): Flow<Schedule> {
        return scheduleDao.getScheduleById(id)
    }

    fun getAllSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getAllSchedules()
    }

    fun getSchedulesByEmployeeId(employeeId: Int): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesByEmployeeId(employeeId)

    }

    fun getSchedulesWithEmployeesForMonth(monthYear: String): Flow<List<Shift>> {
        return scheduleDao.getActiveShiftsForMonth(monthYear)
    }

    fun getSchedulesByDate(date: Date): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesByDate(date)
    }

    fun getShiftsByDate(date: Date): Flow<List<Shift>> {
        return scheduleDao.getShiftByDate(date)
    }

    suspend fun deleteAllSchedules() {
        return scheduleDao.deleteAllSchedules()
    }


    fun getActiveShiftsByDateRange(startDate: Date, endDate: Date): Flow<List<Shift>> {
        return scheduleDao.getActiveShiftByDateRange(startDate, endDate)
    }
}