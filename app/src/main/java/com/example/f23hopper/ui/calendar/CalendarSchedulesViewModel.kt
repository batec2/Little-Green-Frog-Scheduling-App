package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import com.example.f23hopper.data.schedule.ScheduleRepository
import javax.inject.Inject

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CalendarSchedulesViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    var schedules = repository.getAllSchedules()
}
