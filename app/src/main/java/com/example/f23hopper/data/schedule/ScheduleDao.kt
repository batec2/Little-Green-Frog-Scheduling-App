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

    @Query("SELECT * FROM schedules")
    fun getAllSchedulesNonState(): List<Schedule>

    @Transaction
    @Query(
        """
    SELECT schedules.*, employees.* 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE schedules.employeeId = :employeeId 
    """
    )
    fun getSchedulesByEmployeeId(employeeId: Int): Flow<List<Schedule>>

    @Transaction
    @Query(
        """
    SELECT schedules.*, employees.* 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE strftime('%Y-%m', date) = :monthYear AND employees.active = 1
    """
    )
    fun getActiveShiftsForMonth(monthYear: String): Flow<List<Shift>>

    @Transaction
    @Query(
        """
    SELECT schedules.*, employees.* 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE strftime('%Y-%m', date) = :monthYear 
    AND employees.active = 1 
    AND employees.employeeId = :employeeId
    """
    )
    fun getActiveShiftsForEmployee(monthYear: String, employeeId: Int): Flow<List<Shift>>


    @Transaction
    @Query(
        """
    SELECT * 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE schedules.date BETWEEN :startDate AND :endDate AND employees.active = 1
    """
    )
    fun getActiveShiftByDateRange(
        startDate: Date, endDate: Date
    ): Flow<List<Shift>>


    @Transaction
    @Query(
        """
    SELECT * 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE schedules.date BETWEEN :startDate AND :endDate
    """
    )
    fun getAllShiftsByDateRange(
        startDate: Date, endDate: Date
    ): Flow<List<Shift>>

    @Transaction
    @Query(
        """
    SELECT * 
    FROM schedules 
    INNER JOIN employees ON schedules.employeeId = employees.employeeId 
    WHERE schedules.date = :date 
    AND (
        (schedules.date >= CURRENT_DATE AND employees.active = 1) 
        OR 
        (schedules.date < CURRENT_DATE)
    )
    """
    )
    fun getShiftByDate(date: Date): Flow<List<Shift>>

    @Query(" SELECT * FROM schedules WHERE schedules.date = :date AND employeeId = :employeeId")
    fun getSchedulesByEmployee(date: Date, employeeId: Int): Flow<List<Schedule>>

    @Query(" SELECT * FROM schedules WHERE schedules.date = :date")
    fun getSchedulesByDate(date: Date): Flow<List<Schedule>>

    @Query("DELETE FROM schedules")
    suspend fun deleteAllSchedules()

    @Query(
        """
            SELECT *
            FROM schedules
            INNER JOIN employees ON schedules.employeeId = employees.employeeId
            WHERE schedules.date >= :date"""
    )
    fun getShiftFromDate(date: Date): Flow<List<Shift>>

    @Query("SELECT EXISTS( SELECT 1 FROM schedules WHERE id = :id )")
    fun scheduleExistsById(id: Long): Boolean

    @Query("SELECT COUNT(id) as count FROM schedules WHERE date>= :start AND date<= :end AND employeeId = :id")
    fun countOfShifts(id: Long,start: Date,end: Date): Int
}
