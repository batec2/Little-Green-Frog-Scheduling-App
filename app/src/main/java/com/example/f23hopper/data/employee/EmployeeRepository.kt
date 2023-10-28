package com.example.f23hopper.data.employee

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.DayOfWeek
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val employeeDao: EmployeeDao) {

    fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees()
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

    suspend fun insertEmployee(employee: Employee): Long {
        return employeeDao.insert(employee)
    }

    suspend fun updateEmployee(employee: Employee) {
        employeeDao.update(employee)
    }

    suspend fun deactivateEmployee(employee: Employee, value: Int) {
        employeeDao.updateActive(employee.employeeId, value)
    }

    fun getEmployeeById(id: Int): Flow<Employee> {
        return employeeDao.getItem(id)
    }

    fun getEmployeesByDayAndShiftType(day: DayOfWeek, shiftType: ShiftType): Flow<List<Employee>> {
        val query = when (shiftType) {
            ShiftType.DAY, ShiftType.NIGHT -> SimpleSQLiteQuery(
                "SELECT * FROM employees WHERE ${day.name.lowercase()} = ? OR ${day.name.lowercase()} = ?",
                arrayOf(shiftType.name, ShiftType.FULL.name)
            )

            else -> SimpleSQLiteQuery(
                "SELECT * FROM employees WHERE ${day.name.lowercase()} = ?",
                arrayOf(shiftType.name)
            )
        }
        return employeeDao.getEmployeesByDayAndShiftType(query)
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

