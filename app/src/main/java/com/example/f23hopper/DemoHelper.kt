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
suspend fun activateDemoDatabase(db: HopperDatabase) {
    withContext(Dispatchers.IO) {
        val employeeDao = db.employeeDao()
        val scheduleDao = db.scheduleDao()

        // Clear existing data
        wipeDatabase(db)

        // Populate Employees
        val employee1 = Employee(
            firstName = "John",
            lastName = "Doe",
            nickname = "JohnJohn",
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
            nickname = "JaneJane",
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


        val employee3 = Employee(
            firstName = "Alex",
            lastName = "Smith",
            nickname = "Alal",
            email = "alex.smith@example.com",
            phoneNumber = "1122334455",
            canOpen = true,
            canClose = false,
            sunday = ShiftType.FULL,
            monday = ShiftType.DAY,
            tuesday = ShiftType.DAY,
            wednesday = ShiftType.DAY,
            thursday = ShiftType.DAY,
            friday = ShiftType.DAY,
            saturday = ShiftType.FULL
        )

        val employee4 = Employee(
            firstName = "Charlie",
            lastName = "Brown",
            nickname = "CharChar",
            email = "charlie.brown@example.com",
            phoneNumber = "5566778899",
            canOpen = false,
            canClose = true,
            sunday = ShiftType.FULL,
            monday = ShiftType.NIGHT,
            tuesday = ShiftType.NIGHT,
            wednesday = ShiftType.NIGHT,
            thursday = ShiftType.NIGHT,
            friday = ShiftType.NIGHT,
            saturday = ShiftType.FULL
        )


        employee1.employeeId = employeeDao.insert(employee1)
        employee2.employeeId = employeeDao.insert(employee2)
        employee3.employeeId = employeeDao.insert(employee3)
        employee4.employeeId = employeeDao.insert(employee4)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Populate Schedules for the current month
        for (day in 1..daysInMonth) {
            val monthStr = currentMonth.toString().padStart(2, '0')
            val dayStr = day.toString().padStart(2, '0')

            val dateStr = "$currentYear-$monthStr-$dayStr"
            val date = Date.valueOf(dateStr)

            calendar.time = date
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                // Full shifts for weekends
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.FULL.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.FULL.ordinal))
            } else if (day == 1 || day == 10 || day == 14) { // Change 15 to whichever day in October you want
                // Special condition for one day in October
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
            } else {
                // Regular weekdays
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee4.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
            }
        }
    }
}

suspend fun wipeDatabase(db: HopperDatabase) {
    val employeeDao = db.employeeDao()
    val scheduleDao = db.scheduleDao()
    val generalDao = db.hopperDao()

    // delete all schedules, then all employees
    val allSchedules = scheduleDao.getAllSchedules().first()
    for (schedule in allSchedules) {
        scheduleDao.delete(schedule)
    }

    val allEmployees = employeeDao.getAllEmployees().first()
    for (employee in allEmployees) {
        employeeDao.delete(employee)
    }

    // full reset on tables (resets auto-increment)
    val query =
        SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name IN ('employees', 'schedules')")
    generalDao.executeRawQuery(query)
}

