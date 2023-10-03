package com.example.f23hopper.modules

import android.content.Context
import com.example.f23hopper.data.EmployeesDatabase
import com.example.f23hopper.data.employee.EmployeeDao
import com.example.f23hopper.data.employee.EmployeeRepository
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
    fun provideDatabase(@ApplicationContext context: Context): EmployeesDatabase {
        return EmployeesDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideEmployeeRepository(@ApplicationContext context: Context): EmployeeRepository {
        return EmployeeRepository(EmployeesDatabase.getDatabase(context).employeeDao());
    }
}
