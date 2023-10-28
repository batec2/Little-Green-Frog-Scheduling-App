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

    @Query("UPDATE employees Set active = 0 WHERE employeeId = :id")
    suspend fun updateActive(id: Long)

    @Query("SELECT * FROM employees WHERE employeeId = :id")
    fun getItem(id: Int): Flow<Employee>

    @Query("SELECT * FROM employees WHERE active = 1 ORDER BY employees.lastName")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE active = 0 ORDER BY employees.lastName")
    fun getInactiveEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where active = 1 AND " +
            "saturday = \"FULL\" or sunday = \"FULL\" ORDER BY employees.lastName" )
    fun getCanWorkWeekends(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where active = 1 AND" +
            " canClose = 1 ORDER BY employees.lastName")
    fun getCanClose(): Flow<List<Employee>>

    @Query("SELECT * FROM employees Where active = 1 AND " +
            "canOpen = 1 ORDER BY employees.lastName")
    fun getCanOpen(): Flow<List<Employee>>

    @RawQuery(observedEntities = [Employee::class])
    fun getEmployeesByDayAndShiftType(query: SupportSQLiteQuery): Flow<List<Employee>>
}
