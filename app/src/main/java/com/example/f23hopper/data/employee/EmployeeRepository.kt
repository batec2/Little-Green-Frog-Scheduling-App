package com.example.f23hopper.data.employee

import android.util.Log
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val employeeDao: EmployeeDao) {

    fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees()
    }

    fun getAllActiveEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllActiveEmployees()
    }

    fun getInactiveEmployees(): Flow<List<Employee>> {
        return employeeDao.getInactiveEmployees()
    }

    fun getCanWorkWeekends(): Flow<List<Employee>> {
        return employeeDao.getCanWorkWeekends()
    }

    fun getCanOpen(): Flow<List<Employee>> {
        return employeeDao.getCanOpen()
    }

    fun getCanClose(): Flow<List<Employee>> {
        return employeeDao.getCanClose()
    }

    suspend fun isNicknameUsed(nickname: String): Boolean {
        return employeeDao.getEmployeeByNickname(nickname) != null
    }

    @Transaction
    suspend fun upsertEmployee(employee: Employee) {
        // Attempt to update the employee
        val rowsUpdated = employeeDao.update(employee)

        Log.d("upsert", "rows received:${rowsUpdated}")
        // If no rows were updated, the employee doesn't exist, so insert a new one
        if (rowsUpdated == 0) {
            employeeDao.insert(employee)
        }
    }

    suspend fun insertEmployee(employee: Employee): Long {
        return employeeDao.insert(employee)
    }

    suspend fun updateEmployee(employee: Employee) {
        employeeDao.update(employee)
    }

    suspend fun updateEmployeeActive(employee: Employee, value: Int) {
        employeeDao.updateActive(employee.employeeId, value)
    }

    fun getEmployeeById(id: Int): Flow<Employee> {
        return employeeDao.getItem(id)
    }

    fun getAllEmployeesNonState(): List<Employee> {
        return employeeDao.getAllEmployeesNonState()
    }


    fun getEmployeesByDayAndShiftType(
        day: kotlinx.datetime.DayOfWeek,
        shiftType: ShiftType
    ): Flow<List<Employee>> {
        val query = when (shiftType) {
            ShiftType.DAY, ShiftType.NIGHT -> SimpleSQLiteQuery(
                "SELECT * FROM employees WHERE active = 1 AND ${day.name.lowercase()} = ? OR ${day.name.lowercase()} = ?",
                arrayOf(shiftType.name, ShiftType.FULL.name)
            )

            else -> SimpleSQLiteQuery(
                "SELECT * FROM employees WHERE active = 1 AND ${day.name.lowercase()} = ?",
                arrayOf(shiftType.name)
            )
        }
        return employeeDao.getEmployeesByDayAndShiftType(query)
    }



}

