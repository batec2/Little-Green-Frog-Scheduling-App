package com.example.f23hopper.data.schedule

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: Schedule): Long  // Return the Schedule ID

    @Update
    suspend fun update(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("SELECT * FROM schedules WHERE id = :id")
    fun getScheduleById(id: Int): Flow<Schedule>

    @Query("SELECT * FROM schedules")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Transaction
    @Query("SELECT * FROM schedules WHERE employeeId = :employeeId")
    fun getSchedulesByEmployeeId(employeeId: Int): Flow<List<ScheduleWithEmployee>>

    @Transaction
    @Query("SELECT * FROM schedules WHERE strftime('%Y-%m', date) = :monthYear")
    fun getSchedulesWithEmployeesForMonth(monthYear: String): Flow<List<ScheduleWithEmployee>>


    @Transaction
    @Query("SELECT * FROM schedules WHERE schedules.date BETWEEN :startDate AND :endDate")
    fun getSchedulesWithEmployeesByDateRange(
        startDate: Date,
        endDate: Date
    ): Flow<List<ScheduleWithEmployee>>

    @Query(" SELECT * FROM schedules WHERE schedules.date = :date")
    fun getSchedulesWithEmployeesByDate(date: Date): Flow<List<Schedule>>

    @Query("DELETE FROM schedules")
    suspend fun deleteAllSchedules()

}
