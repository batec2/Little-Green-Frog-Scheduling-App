package com.example.f23hopper

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.Calendar


// Adds two employees, and fills out every other day of the current month with their shifts.
// Will likely need to be tweaked as we go along, but good for keeping our data at a shared state
// until we're further.
suspend fun populateDatabase(db: HopperDatabase) {
    withContext(Dispatchers.IO) {
        // Clear existing data
        val employeeDao = db.employeeDao()
        val scheduleDao = db.scheduleDao()
        wipeDatabase(db)

        // Populate Employees
        val employee1 = Employee(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            phoneNumber = "1234567890",
            canOpen = true,
            canClose = false,
            sunday = ShiftType.DAY,
            monday = ShiftType.DAY,
            tuesday = ShiftType.DAY,
            wednesday = ShiftType.DAY,
            thursday = ShiftType.DAY,
            friday = ShiftType.DAY,
            saturday = ShiftType.DAY
        )
        val employee2 = Employee(
            firstName = "Jane",
            lastName = "Doe",
            email = "jane.doe@example.com",
            phoneNumber = "0987654321",
            canOpen = false,
            canClose = true,
            sunday = ShiftType.NIGHT,
            monday = ShiftType.NIGHT,
            tuesday = ShiftType.NIGHT,
            wednesday = ShiftType.NIGHT,
            thursday = ShiftType.NIGHT,
            friday = ShiftType.NIGHT,
            saturday = ShiftType.NIGHT
        )
        employee1.employeeId = employeeDao.insert(employee1)
        employee2.employeeId = employeeDao.insert(employee2)
        employeeDao.insert(employee2)

        // using a Calendar instance (not ours) to find out the current month and year
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Initialize skipping logic
        var skip = false

        // Populate Schedules for the current month
        for (day in 1..daysInMonth) {
            if (skip) {
                skip = false
                continue
            }

            // get str for Date parsing
            val monthStr = currentMonth.toString().padStart(2, '0')
            val dayStr = day.toString().padStart(2, '0')

            // ex. 2023-10-04
            val dateStr = "$currentYear-$monthStr-$dayStr"
            val date = Date.valueOf(dateStr)

            calendar.time = date
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // determine shift type based on the day of the week
            // e.g. weekends are long.
            val shiftType1: Int
            val shiftType2: Int
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                shiftType1 = ShiftType.FULL.ordinal
                shiftType2 = ShiftType.FULL.ordinal
            } else {
                shiftType1 = ShiftType.DAY.ordinal
                shiftType2 = ShiftType.NIGHT.ordinal
            }

            val schedule1 =
                Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = shiftType1)
            val schedule2 =
                Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = shiftType2)

            scheduleDao.insert(schedule1)
            scheduleDao.insert(schedule2)

            // skip the next day, just for fun and variation.
            skip = true
        }
    }
}

suspend fun wipeDatabase(db: HopperDatabase) {
    val employeeDao = db.employeeDao()
    val scheduleDao = db.scheduleDao()
    val generalDao = db.hopperDao()

    // Delete all schedules
    val allSchedules = scheduleDao.getAllSchedules().first()
    for (schedule in allSchedules) {
        scheduleDao.delete(schedule)
    }

    // Delete all employees
    val allEmployees = employeeDao.getAllEmployees().first()
    for (employee in allEmployees) {
        employeeDao.delete(employee)
    }

    // Reset auto-increment IDs
    val query = SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name IN ('employees', 'schedules')")
    generalDao.executeRawQuery(query)
}

