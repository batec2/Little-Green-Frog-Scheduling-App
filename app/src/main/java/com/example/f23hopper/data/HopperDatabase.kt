package com.example.f23hopper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.employee.EmployeeDao
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleDao
import com.example.f23hopper.data.shifttype.ShiftTypeConverter
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayDao
import com.example.f23hopper.utils.DateTypeConverter

@Database(
    entities = [Employee::class, Schedule::class, SpecialDay::class],
    version = 1,
    exportSchema = false/*disable schema ver history*/
)
@TypeConverters(WeekDayConverter::class, ShiftTypeConverter::class, DateTypeConverter::class)
abstract class HopperDatabase : RoomDatabase()/*Extends RoomDatabase class*/ {

    abstract fun employeeDao(): EmployeeDao//tells database about dao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun specialDayDao(): SpecialDayDao
    abstract fun hopperDao(): HopperDao


    /*Allows access to methods to create or get database*/
    companion object {
        //Keeps a reference to database,helps maintain single instance of database
        @Volatile //make so variable is not cached, ensures instance upt to date for all threads
        private var Instance: HopperDatabase? = null
        fun getDatabase(context: Context): HopperDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HopperDatabase::class.java, "employees_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}