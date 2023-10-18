package com.example.f23hopper.ui.shiftedit

import androidx.lifecycle.ViewModel
import com.example.f23hopper.data.schedule.ScheduleRepository
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDayRepository
import com.example.f23hopper.utils.toSqlDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class ShiftEditViewModel @Inject constructor(
    private val repository: ScheduleRepository,

    private val specialDayRepo: SpecialDayRepository
) : ViewModel() {
    fun getShiftsForDay(date: LocalDate): Flow<List<Shift>> {
        return repository.getShiftsByDate(date.toSqlDate())
    }

    suspend fun isSpecialDay(date: LocalDate): Boolean {
        return specialDayRepo.isDateInTable(date.toSqlDate())
    }
}
