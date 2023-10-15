package com.example.f23hopper.data.employee

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(employee: Employee): Long  //return the Employee ID

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees WHERE employeeId = :id")
    fun getItem(id: Int): Flow<Employee>

    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where saturday = \"FULL\" or sunday = \"FULL\"")
    fun getCanWorkWeekends(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where canClose = 1")
    fun getCanClose(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where canOpen = 1")
    fun getCanOpen(): Flow<List<Employee>>

    @RawQuery(observedEntities = [Employee::class])
    fun getEmployeesByDayAndShiftType(query: SupportSQLiteQuery): Flow<List<Employee>>
}
