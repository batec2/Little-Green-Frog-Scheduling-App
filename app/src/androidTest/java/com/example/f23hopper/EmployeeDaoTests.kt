package com.example.f23hopper

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.f23hopper.data.DayOfWeek
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeDao
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EmployeeDaoTest {

    private lateinit var db: HopperDatabase
    private lateinit var dao: EmployeeDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HopperDatabase::class.java
        ).build()
        dao = db.employeeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetEmployee() = runBlocking {
        val employee = Employee(
            firstName = "John",
            lastName = "Doe",
            nickname = "johnnyjohnny",
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

        dao.insert(employee)
        val generatedId = dao.insert(employee)

        val retrievedEmployee = dao.getItem(generatedId.toInt()).first()
        assertTrue(retrievedEmployee == employee.copy(employeeId = generatedId))
    }

    @Test
    @Throws(Exception::class)
    fun testGetEmployeesByDayAndShiftType() = runBlocking {
        val employee1 = Employee(
            firstName = "John",
            lastName = "Doe",
            nickname = "johnjohnny",
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
            nickname = "janejane",
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

        val repo = EmployeeRepository(dao)

        repo.insertEmployee(employee1)
        repo.insertEmployee(employee2)

        val allEmployees = repo.getAllEmployees().first()
        println("All employees: $allEmployees")

        val employees = repo.getEmployeesByDayAndShiftType(DayOfWeek.SUNDAY, ShiftType.DAY).first()

        assertTrue(employees.size == 1)
        assertTrue(employees[0].sunday == ShiftType.DAY)
    }

}
