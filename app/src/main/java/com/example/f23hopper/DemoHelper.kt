package com.example.f23hopper

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeDao
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleDao
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayDao
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
        val (employeeDao, scheduleDao, specialDayDao) = initializeDaos(db)

        // Clear existing data
        wipeDatabase(db)

        // Populate Employees
        val employees = createEmployees()

        // Insert Employees
        insertEmployees(employees, employeeDao)

        // Populate Schedules for the current month
        populateSchedules(scheduleDao, specialDayDao, employees)
    }
}

fun createEmployees(): List<Employee> {

    // Populate Employees
    val employee1 = Employee(
        firstName = "John",
        lastName = "Doe",
        nickname = "Johnny",
        email = "john.doe@example.com",
        phoneNumber = "1234567890",
        maxShifts = "0",
        canOpen = true,
        canClose = false,
        sunday = ShiftType.FULL,
        monday = ShiftType.FULL,
        tuesday = ShiftType.FULL,
        wednesday = ShiftType.FULL,
        thursday = ShiftType.FULL,
        friday = ShiftType.FULL,
        saturday = ShiftType.FULL,
        active = true
    )
    val employee2 = Employee(
        firstName = "Jane",
        lastName = "Doe",
        nickname = "Janey",
        email = "jane.doe@example.com",
        phoneNumber = "0987654321",
        maxShifts = "0",
        canOpen = false,
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


    val employee3 = Employee(
        firstName = "John",
        lastName = "Smith",
        nickname = "JoJo",
        email = "John.Smith@example.com",
        phoneNumber = "52344442134",
        maxShifts = "0",
        canOpen = true,
        canClose = false,
        sunday = ShiftType.CANT_WORK,
        monday = ShiftType.DAY,
        tuesday = ShiftType.DAY,
        wednesday = ShiftType.CANT_WORK,
        thursday = ShiftType.NIGHT,
        friday = ShiftType.NIGHT,
        saturday = ShiftType.FULL,
        active = true
    )

    val employee4 = Employee(
        firstName = "Charlie",
        lastName = "Brown",
        nickname = "Chuck",
        email = "charlie.brown@example.com",
        phoneNumber = "5566778899",
        maxShifts = "0",
        canOpen = false,
        canClose = true,
        sunday = ShiftType.FULL,
        monday = ShiftType.NIGHT,
        tuesday = ShiftType.NIGHT,
        wednesday = ShiftType.DAY,
        thursday = ShiftType.CANT_WORK,
        friday = ShiftType.DAY,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee5 = Employee(
        firstName = "Bob",
        lastName = "Builder",
        nickname = "Bobby",
        email = "Bob.Builder@example.com",
        phoneNumber = "20389201",
        maxShifts = "0",
        canOpen = true,
        canClose = true,
        sunday = ShiftType.CANT_WORK,
        monday = ShiftType.DAY,
        tuesday = ShiftType.CANT_WORK,
        wednesday = ShiftType.DAY,
        thursday = ShiftType.NIGHT,
        friday = ShiftType.NIGHT,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee6 = Employee(
        firstName = "Steve",
        lastName = "Crafter",
        nickname = "Blockhead",
        email = "steve.miner@example.com",
        phoneNumber = "4566575242",
        maxShifts = "0",
        canOpen = true,
        canClose = true,
        sunday = ShiftType.CANT_WORK,
        monday = ShiftType.CANT_WORK,
        tuesday = ShiftType.NIGHT,
        wednesday = ShiftType.NIGHT,
        thursday = ShiftType.CANT_WORK,
        friday = ShiftType.CANT_WORK,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee7 = Employee(
        firstName = "AJ",
        lastName = "Bruningham",
        nickname = "AJ",
        email = "AJ@example.com",
        phoneNumber = "1245777777",
        maxShifts = "0",
        canOpen = true,
        canClose = true,
        sunday = ShiftType.FULL,
        monday = ShiftType.NIGHT,
        tuesday = ShiftType.CANT_WORK,
        wednesday = ShiftType.FULL,
        thursday = ShiftType.FULL,
        friday = ShiftType.CANT_WORK,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee8 = Employee(
        firstName = "Victoria",
        lastName = "Secretur",
        nickname = "Vicky",
        email = "VickyVic@example.com",
        phoneNumber = "7802342342",
        maxShifts = "0",
        canOpen = true,
        canClose = true,
        sunday = ShiftType.CANT_WORK,
        monday = ShiftType.DAY,
        tuesday = ShiftType.DAY,
        wednesday = ShiftType.NIGHT,
        thursday = ShiftType.NIGHT,
        friday = ShiftType.CANT_WORK,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee9 = Employee(
        firstName = "Jason",
        lastName = "Todd",
        nickname = "Red",
        email = "BatmanFan#1@example.com",
        phoneNumber = "3242346757",
        maxShifts = "0",
        canOpen = true,
        canClose = true,
        sunday = ShiftType.CANT_WORK,
        monday = ShiftType.DAY,
        tuesday = ShiftType.DAY,
        wednesday = ShiftType.NIGHT,
        thursday = ShiftType.FULL,
        friday = ShiftType.FULL,
        saturday = ShiftType.CANT_WORK,
        active = true
    )

    val employee10 = Employee(
        firstName = "Jason",
        lastName = "Bourne",
        nickname = "JDawg",
        email = "itsJasonBourne@example.com",
        phoneNumber = "2342347567",
        maxShifts = "0",
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

    return listOf(
        employee1,
        employee2,
        employee3,
        employee4,
        employee5,
        employee6,
        employee7,
        employee8,
        employee9,
        employee10,
    )
}

suspend fun initializeDaos(db: HopperDatabase): Triple<EmployeeDao, ScheduleDao, SpecialDayDao> {
    val employeeDao = db.employeeDao()
    val scheduleDao = db.scheduleDao()
    val specialDayDao = db.specialDayDao()
    return Triple(employeeDao, scheduleDao, specialDayDao)
}

suspend fun insertEmployees(employees: List<Employee>, employeeDao: EmployeeDao) {
    employees.forEach { employee ->
        employee.employeeId = employeeDao.insert(employee)

    }
}

suspend fun populateSchedules(
    scheduleDao: ScheduleDao,
    specialDayDao: SpecialDayDao,
    employees: List<Employee>
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar[Calendar.YEAR]
    val currentMonth = calendar[Calendar.MONTH] + 1
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Create a rotating index for employees
    var rotatingIndex = 0

    (1..daysInMonth).forEach { day ->
        val monthStr = currentMonth.toString().padStart(2, '0')
        val dayStr = day.toString().padStart(2, '0')
        val dateStr = "$currentYear-$monthStr-$dayStr"
        val date = Date.valueOf(dateStr)
        calendar.time = date
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY

        when {
            day == 10 -> {
                // Special Day handling
                employees.subList(0, 6).forEachIndexed { index, employee ->
                    val shiftType = if (index < 3) ShiftType.DAY else ShiftType.NIGHT
                    scheduleDao.insert(
                        Schedule(
                            date = date,
                            employeeId = employee.employeeId,
                            shiftType = shiftType
                        )
                    )
                }
                specialDayDao.insert(SpecialDay(date = date))
            }
            day == 2 -> {
                // Only 3 employees scheduled on day 2
                (0 until 3).forEach { i ->
                    val employee = employees[(rotatingIndex + i -1) % employees.size]
                    val shiftType = if (i < 2) ShiftType.DAY else ShiftType.NIGHT
                    scheduleDao.insert(
                        Schedule(
                            date = date,
                            employeeId = employee.employeeId,
                            shiftType = shiftType
                        )
                    )
                }
                // Increment rotatingIndex by 3 for the next day
                rotatingIndex += 3
            }
            isWeekend -> {
                // Only two full shifts needed on weekends
                (0 until 2).forEach { i ->
                    val employee = employees[(rotatingIndex + i) % employees.size]
                    scheduleDao.insert(
                        Schedule(
                            date = date,
                            employeeId = employee.employeeId,
                            shiftType = ShiftType.FULL
                        )
                    )
                }
                // Increment rotatingIndex by 2 for the next day
                rotatingIndex += 2
            }
            else -> {
                // Assign two day shifts and two night shifts for each regular weekday
                (0 until 4).forEach { i ->
                    val employee = employees[(rotatingIndex + i) % employees.size]
                    val shiftType = if (i < 2) ShiftType.DAY else ShiftType.NIGHT
                    scheduleDao.insert(
                        Schedule(
                            date = date,
                            employeeId = employee.employeeId,
                            shiftType = shiftType
                        )
                    )
                }
                // Increment rotatingIndex by 4 for the next day
                rotatingIndex += 4
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
        SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name IN ('employees', 'schedules')")
    generalDao.executeRawQuery(query)
}