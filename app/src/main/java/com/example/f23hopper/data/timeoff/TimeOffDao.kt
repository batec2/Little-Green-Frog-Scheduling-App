package com.example.f23hopper.data.timeoff

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

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

}