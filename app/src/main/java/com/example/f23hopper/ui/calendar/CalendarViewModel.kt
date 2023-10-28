package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val specialDayRepo: SpecialDayRepository
) : ViewModel() {



    private val startDate: Date = getStartDate()
    private val endDate: Date = getEndDate()

    val parsedShifts: StateFlow<List<Shift>> by lazy { parseShifts(fetchRawShifts()) }
    val parsedDays: StateFlow<List<SpecialDay>> by lazy { parseSpecialDays(fetchRawSpecialDays()) }

    private fun getStartDate(): Date {
        val currentDate = LocalDate.now()
        return currentDate.minusMonths(2).withDayOfMonth(1).toSqlDate()
    }

    private fun getEndDate(): Date {
        val currentDate = LocalDate.now()
        return currentDate.plusMonths(12).withDayOfMonth(1).minusDays(1)
            .toSqlDate()
    }

    private fun fetchRawShifts(): Flow<List<Shift>> {
        return scheduleRepo.getShiftsByDateRange(startDate, endDate)
    }

    private fun parseShifts(rawShifts: Flow<List<Shift>>): StateFlow<List<Shift>> {
        return rawShifts
            .map { shifts -> shifts.sortedBy { it.schedule.shiftTypeId } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private fun fetchRawSpecialDays(): Flow<List<SpecialDay>> {
        return specialDayRepo.getSpecialDays()
    }

    private fun parseSpecialDays(
        rawSpecialDays: Flow<List<SpecialDay>>
    ): StateFlow<List<SpecialDay>> {
        return rawSpecialDays
            .map { days -> days.sortedBy { it.date } }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }
}