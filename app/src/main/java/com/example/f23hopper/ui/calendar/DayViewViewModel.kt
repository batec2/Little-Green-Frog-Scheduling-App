package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.schedule.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class DayViewViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    fun getEventsForClickedDay(clickedDay: LocalDate): Flow<List<Schedule>> {
        return repository.getSchedulesByDate(Date.valueOf(clickedDay.toJavaLocalDate().toString()))
    }
}
