package com.example.f23hopper.data.specialDay

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecialDayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(specialDay: SpecialDay): SpecialDay {
        return specialDay
    }


    @Update
    suspend fun update(specialDay: SpecialDay)

    @Delete
    suspend fun delete(specialDay: SpecialDay)

    @Query("SELECT * FROM specialdays")
    fun getSpecialDays(): Flow<List<SpecialDay>>

    @Query("DELETE FROM specialdays")
    suspend fun deleteAllSpecialDays()

}
