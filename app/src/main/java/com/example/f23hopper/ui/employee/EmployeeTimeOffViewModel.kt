package com.example.f23hopper.ui.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.asLiveData
import com.example.f23hopper.data.timeoff.TimeOffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class EmployeeTimeOffViewModel @Inject constructor(
    private val timeOffRepository: TimeOffRepository
){
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    var timeOffList by mutableStateOf(timeOffRepository.getAllTimeOff().asLiveData())
}