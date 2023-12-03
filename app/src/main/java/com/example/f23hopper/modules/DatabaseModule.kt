package com.example.f23hopper.modules

import android.content.Context
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.data.timeoff.TimeOffRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HopperDatabase {
        return HopperDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideEmployeeRepository(@ApplicationContext context: Context): EmployeeRepository {
        return EmployeeRepository(HopperDatabase.getDatabase(context).employeeDao())
    }


    @Provides
    @Singleton
    fun provideSpecialDayRepository(@ApplicationContext context: Context): SpecialDayRepository {
        return SpecialDayRepository(HopperDatabase.getDatabase(context).specialDayDao())
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(@ApplicationContext context: Context): ScheduleRepository {
        return ScheduleRepository(HopperDatabase.getDatabase(context).scheduleDao())
    }

    @Provides
    @Singleton
    fun provideTimeOffRepository(@ApplicationContext context: Context): TimeOffRepository {
        return TimeOffRepository(HopperDatabase.getDatabase(context).timeOffDao())
    }

}
