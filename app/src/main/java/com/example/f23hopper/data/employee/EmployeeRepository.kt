package com.example.f23hopper.data.employee

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.DayOfWeek
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val employeeDao: EmployeeDao) {

    fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees()
    }

    suspend fun insertEmployee(employee: Employee): Long {
        return employeeDao.insert(employee)
    }

    suspend fun updateEmployee(employee: Employee) {
        employeeDao.update(employee)
    }

    suspend fun deleteEmployee(employee: Employee) {
        employeeDao.delete(employee)
    }

    fun getEmployeeById(id: Int): Flow<Employee> {
        return employeeDao.getItem(id)
    }

    fun getEmployeesByDayAndShiftType(day: DayOfWeek, shiftType: ShiftType): Flow<List<Employee>> {
        val query = SimpleSQLiteQuery("SELECT * FROM employees WHERE ${day.name.lowercase()} = ?", arrayOf(shiftType.name))
        return employeeDao.getEmployeesByDayAndShiftType(query)
    }
}
