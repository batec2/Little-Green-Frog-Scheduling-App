package com.example.f23hopper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.f23hopper.utils.DateTypeConverter

@Database(entities = [Employee::class,  Schedule::class], version = 1, exportSchema = false/*disable schema ver history*/)
@TypeConverters(WeekDayConverter::class, ShiftTypeConverter::class, DateTypeConverter::class)
abstract class EmployeesDatabase : RoomDatabase()/*Extends RoomDatabase class*/ {

    abstract fun employeeDao(): EmployeeDao//tells database about dao
    abstract fun scheduleDao(): ScheduleDao
    /*Allows access to methods to create or get database*/
    companion object {
        //Keeps a reference to database,helps maintain single instance of database
        @Volatile //make so variable is not cached, ensures instance upt to date for all threads
        private var Instance: EmployeesDatabase? = null
        fun getDatabase(context: Context): EmployeesDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, EmployeesDatabase::class.java, "employees_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}