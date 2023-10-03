package com.example.f23hopper.modules

import android.content.Context
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.data.employee.EmployeeRepository
import com.example.f23hopper.data.schedule.ScheduleRepository
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
    fun provideScheduleRepository(@ApplicationContext context: Context): ScheduleRepository {
        return ScheduleRepository(HopperDatabase.getDatabase(context).scheduleDao())
    }
}
