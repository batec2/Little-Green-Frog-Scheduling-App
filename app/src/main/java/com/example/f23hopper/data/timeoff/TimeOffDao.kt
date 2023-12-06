package com.example.f23hopper.data.timeoff

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface TimeOffDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(timeOff: TimeOff)

    @Update
    suspend fun update(timeOff: TimeOff)

    @Delete
    suspend fun delete(timeOff: TimeOff)

    @Query("SELECT * FROM timeoff")
    fun getTimeOff(): Flow<List<TimeOff>>

    @Query("SELECT * FROM timeoff")
    fun getTimeOffNonState(): List<TimeOff>

    @Query("SELECT * FROM timeoff WHERE dateFrom <= :date AND dateTo >= :date")
    fun getTimeOffByDate(date: Date): Flow<List<TimeOff>>

    @Query(
        "SELECT COUNT(id) as count FROM timeoff " +
                "WHERE (dateFrom>= :start AND dateFrom<= :end) OR (dateTo>= :start AND dateTo<= :end)" +
                "AND employeeId = :id"
    )
    fun countOfTimeOff(id: Long, start: Date, end: Date): Int
}