package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.schedule.Schedule
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

//TODO: Pass in the events directly instead of having it make a DB call. Might be a lot snappier that way.
// Test if that approach works for when a schedule is added though.
@Composable
fun WeekViewScreen(clickedDay: LocalDate) {
    val viewModel = hiltViewModel<DayViewViewModel>()
    val dayEventsFlow: Flow<List<Schedule>> = viewModel.getEventsForClickedDay(clickedDay)
    val dayEvents by dayEventsFlow.collectAsState(initial = emptyList())


    Column {
        Text("TODO: $clickedDay", style = MaterialTheme.typography.headlineSmall)
        LazyColumn {
            items(dayEvents) { event ->
                ScheduleCard(event)
            }
        }
        Button(onClick = { /* navigate to add new schedule screen */ }) {
            Text("Add New Event")
        }
    }


    @Composable
    fun ScheduleList(clickedDay: LocalDate?, schedules: List<Schedule>) {
        if (clickedDay != null) {
            Text("Events on $clickedDay")
            LazyColumn {
                items(schedules) { schedule ->
                    ScheduleCard(schedule)
                }
            }
        }
    }
}


@Composable
fun ScheduleCard(schedule: Schedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Employee ID: ${schedule.employeeId}, Shift Type: ${ShiftType.entries[schedule.shiftTypeId]}")
    }
}
