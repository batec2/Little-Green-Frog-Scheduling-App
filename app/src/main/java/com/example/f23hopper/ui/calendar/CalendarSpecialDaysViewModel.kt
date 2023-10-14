package com.example.f23hopper.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.ScheduleWithEmployee
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarSpecialDaysViewModel @Inject constructor(
    private val repository: SpecialDayRepository
) : ViewModel() {

    private val _rawSpecialDays = repository.getSpecialDays()

    val parsedDays: StateFlow<List<SpecialDay>> =
        _rawSpecialDays
            .map { specialDays ->
                specialDays.sortedBy { it.date }
            }
            .flowOn(Dispatchers.Default) // Ensure computation is done in the background
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

