package com.example.f23hopper

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.f23hopper.data.*
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeDao
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleDao
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.sql.Date

@RunWith(AndroidJUnit4::class)
class ScheduleDaoTest {

    private lateinit var db: HopperDatabase
    private lateinit var scheduleDao: ScheduleDao
    private lateinit var employeeDao: EmployeeDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HopperDatabase::class.java
        ).build()
        scheduleDao = db.scheduleDao()
        employeeDao = db.employeeDao()

        // Inserting an Employee to satisfy foreign key constraints
        runBlocking {
            val employee = Employee(
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
            employeeDao.insert(employee)
            employeeDao.insert(employee2)
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetSchedule() = runBlocking {
        val schedule = Schedule(
            date = Date.valueOf("2023-10-01"),
            employeeId = 1,
            shiftTypeId = 1
        )

        val generatedId = scheduleDao.insert(schedule)

        val retrievedSchedule = scheduleDao.getScheduleById(generatedId.toInt()).first()
        assertTrue(retrievedSchedule == schedule.copy(id = generatedId.toInt()))
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateSchedule() = runBlocking {
        val schedule = Schedule(
            date = Date.valueOf("2023-10-01"),
            employeeId = 1,
            shiftTypeId = 1
        )

        val generatedId = scheduleDao.insert(schedule)

        val updatedSchedule = schedule.copy(
            id = generatedId.toInt(),
            date = Date.valueOf("2023-10-02")
        )

        scheduleDao.update(updatedSchedule)

        val retrievedSchedule = scheduleDao.getScheduleById(generatedId.toInt()).first()
        assertTrue(retrievedSchedule == updatedSchedule)
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteSchedule() = runBlocking {
        val schedule = Schedule(
            date = Date.valueOf("2023-10-01"),
            employeeId = 1,
            shiftTypeId = 1
        )

        val generatedId = scheduleDao.insert(schedule)

        val retrievedSchedule = scheduleDao.getScheduleById(generatedId.toInt()).first()
        scheduleDao.delete(retrievedSchedule)

        val allSchedules = scheduleDao.getAllSchedules().first()
        assertTrue(allSchedules.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllSchedules() = runBlocking {
        val schedule1 = Schedule(
            date = Date.valueOf("2023-10-01"),
            employeeId = 1,
            shiftTypeId = 1
        )
        val schedule2 = Schedule(
            date = Date.valueOf("2023-10-02"),
            employeeId = 2,
            shiftTypeId = 2
        )

        scheduleDao.insert(schedule1)
        scheduleDao.insert(schedule2)

        val allSchedules = scheduleDao.getAllSchedules().first()
        assertTrue(allSchedules.size == 2)
    }
}
