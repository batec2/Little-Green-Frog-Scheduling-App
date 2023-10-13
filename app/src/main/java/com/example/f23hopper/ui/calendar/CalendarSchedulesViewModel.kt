package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.ScheduleWithEmployee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarSchedulesViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    // helper function to convert java.time.LocalDate to java.sql.Date
    private fun LocalDate.toSqlDate(): Date = Date.valueOf(this.toString())

    // determine the start and end dates for the current year ± 2 months.
    // NOTE: I don't think this logic is correct, needs testing.
    private val currentDate = LocalDate.now()
    private val startDate = currentDate.minusMonths(2).withDayOfMonth(1).toSqlDate()
    private val endDate = currentDate.plusMonths(12).withDayOfMonth(1).minusDays(1).toSqlDate()

    // fetch schedules within the specified date range.
    private val _rawSchedulesWithEmployees =
        repository.getSchedulesWithEmployeesByDateRange(startDate, endDate)

    val parsedEvents: StateFlow<List<ScheduleWithEmployee>> =
        _rawSchedulesWithEmployees
            .map { schedulesWithEmployees ->
                schedulesWithEmployees.sortedBy { it.schedule.shiftTypeId }
            }
            .flowOn(Dispatchers.Default) // makes sure that computation is done in the background
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
