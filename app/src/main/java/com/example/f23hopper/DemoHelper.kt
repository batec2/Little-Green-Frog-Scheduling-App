package com.example.f23hopper

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
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
        val specialDayDao = db.specialDayDao()

        // Clear existing data
        wipeDatabase(db)

        // Populate Employees
        val employee1 = Employee(
            employeeId = 1,
            firstName = "John",
            lastName = "Doe",
            nickname = "",
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
            saturday = ShiftType.DAY,
            active = true
        )
        val employee2 = Employee(
            employeeId = 2,
            firstName = "Jane",
            lastName = "Doe",
            nickname = "",
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
            saturday = ShiftType.NIGHT,
            active = true
        )


        val employee3 = Employee(
            employeeId = 3,
            firstName = "John",
            lastName = "Smith",
            nickname = "JoJo",
            email = "John.Smith@example.com",
            phoneNumber = "52344442134",
            canOpen = true,
            canClose = false,
            sunday = ShiftType.FULL,
            monday = ShiftType.DAY,
            tuesday = ShiftType.DAY,
            wednesday = ShiftType.DAY,
            thursday = ShiftType.DAY,
            friday = ShiftType.DAY,
            saturday = ShiftType.FULL,
            active = true
        )

        val employee4 = Employee(
            employeeId = 4,
            firstName = "Charlie",
            lastName = "Brown",
            nickname = "",
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
            saturday = ShiftType.FULL,
            active = true
        )

        val employee5 = Employee(
            employeeId = 5,
            firstName = "Bob",
            lastName = "Builder",
            nickname = "",
            email = "Bob.Builder@example.com",
            phoneNumber = "0000000000",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.FULL,
            monday = ShiftType.FULL,
            tuesday = ShiftType.FULL,
            wednesday = ShiftType.FULL,
            thursday = ShiftType.FULL,
            friday = ShiftType.FULL,
            saturday = ShiftType.FULL,
            active = true
        )

        val employee6 = Employee(
            employeeId = 6,
            firstName = "Steve",
            lastName = "Crafter",
            nickname = "",
            email = "steve.miner@example.com",
            phoneNumber = "4566575242",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.CANT_WORK,
            monday = ShiftType.CANT_WORK,
            tuesday = ShiftType.CANT_WORK,
            wednesday = ShiftType.CANT_WORK,
            thursday = ShiftType.FULL,
            friday = ShiftType.FULL,
            saturday = ShiftType.FULL,
            active = true
        )

        val employee7 = Employee(
            employeeId = 7,
            firstName = "AJ",
            lastName = "Bruningham",
            nickname = "",
            email = "AJ@example.com",
            phoneNumber = "1245777777",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.FULL,
            monday = ShiftType.CANT_WORK,
            tuesday = ShiftType.CANT_WORK,
            wednesday = ShiftType.CANT_WORK,
            thursday = ShiftType.CANT_WORK,
            friday = ShiftType.CANT_WORK,
            saturday = ShiftType.FULL,
            active = true
        )

        val employee8 = Employee(
            employeeId = 8,
            firstName = "Victoria",
            lastName = "Secretur",
            nickname = "Vicky",
            email = "VickyVic@example.com",
            phoneNumber = "7802342342",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.FULL,
            monday = ShiftType.NIGHT,
            tuesday = ShiftType.FULL,
            wednesday = ShiftType.DAY,
            thursday = ShiftType.FULL,
            friday = ShiftType.DAY,
            saturday = ShiftType.FULL,
            active = true
        )

        val employee9 = Employee(
            employeeId = 9,
            firstName = "Jason",
            lastName = "Todd",
            nickname = "Red",
            email = "BatmanFan#1@example.com",
            phoneNumber = "3242346757",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.CANT_WORK,
            monday = ShiftType.FULL,
            tuesday = ShiftType.FULL,
            wednesday = ShiftType.FULL,
            thursday = ShiftType.FULL,
            friday = ShiftType.FULL,
            saturday = ShiftType.CANT_WORK,
            active = true
        )

        val employee10 = Employee(
            employeeId = 10,
            firstName = "Jason",
            lastName = "Bourne",
            nickname = "",
            email = "itsJasonBourne@example.com",
            phoneNumber = "2342347567",
            canOpen = true,
            canClose = true,
            sunday = ShiftType.CANT_WORK,
            monday = ShiftType.FULL,
            tuesday = ShiftType.FULL,
            wednesday = ShiftType.FULL,
            thursday = ShiftType.FULL,
            friday = ShiftType.FULL,
            saturday = ShiftType.CANT_WORK,
            active = true
        )




        employee1.employeeId = employeeDao.insert(employee1)
        employee2.employeeId = employeeDao.insert(employee2)
        employee3.employeeId = employeeDao.insert(employee3)
        employee4.employeeId = employeeDao.insert(employee4)
        employee5.employeeId = employeeDao.insert(employee5)
        employee6.employeeId = employeeDao.insert(employee6)
        employee7.employeeId = employeeDao.insert(employee7)
        employee8.employeeId = employeeDao.insert(employee8)
        employee9.employeeId = employeeDao.insert(employee9)
        employee10.employeeId = employeeDao.insert(employee10)

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

            if (day == 10) {
                // Special Day in October
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.NIGHT.ordinal))
                specialDayDao.insert(SpecialDay(date = date))
            }
            else if (day == 11) {
                // Incomplete weekday shift
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.DAY.ordinal))
            }
            else if (day == 15) {
                // Incomplete weekend shift
                scheduleDao.insert(Schedule(date = date, employeeId = employee3.employeeId, shiftTypeId = ShiftType.FULL.ordinal))
            }
            else if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                // Completed weekend shifts
                scheduleDao.insert(Schedule(date = date, employeeId = employee1.employeeId, shiftTypeId = ShiftType.FULL.ordinal))
                scheduleDao.insert(Schedule(date = date, employeeId = employee2.employeeId, shiftTypeId = ShiftType.FULL.ordinal))
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
    val specialDayDao = db.specialDayDao()
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

    // Delete all special days
    val allSpecialDays = specialDayDao.getSpecialDays().first()
    for (specialDay in allSpecialDays) {
        specialDayDao.delete(specialDay)
    }

    // full reset on tables (resets auto-increment)
    val query =
        // TODO: Will including 'specialdays' cause an issue here?
        SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name IN ('employees', 'schedules')")
    generalDao.executeRawQuery(query)
}

