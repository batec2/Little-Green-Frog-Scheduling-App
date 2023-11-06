package com.example.f23hopper.data.specialDay

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface SpecialDayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(specialDay: SpecialDay)

    @Update
    suspend fun update(specialDay: SpecialDay)

    @Delete
    suspend fun delete(specialDay: SpecialDay)

    @Query("SELECT * FROM specialdays")
    fun getSpecialDays(): Flow<List<SpecialDay>>

    @Query("SELECT * FROM specialdays WHERE date = :date")
    fun getSpecialDay(date: Date): SpecialDay?

    @Query("SELECT * FROM specialdays WHERE date > :startDate and date < :endDate")
    fun getSpecialDaysByDateRange(startDate: Date, endDate: Date): Flow<List<SpecialDay>>

    @Query("SELECT EXISTS(SELECT 1 FROM specialdays WHERE date = :date)")
    fun observeIsSpecialDay(date: Date): Flow<Boolean>


    @Query("DELETE FROM specialdays")
    suspend fun deleteAllSpecialDays()

    @Query("SELECT COUNT(date) FROM specialdays WHERE date = :date")
    suspend fun countDate(date: Date): Int

}
