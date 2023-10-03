package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.shifttype.ShiftType
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun CalendarScreen() {

    val viewModel = hiltViewModel<CalendarSchedulesViewModel>()
    val schedulesFromDb by viewModel.schedules.collectAsState(initial = emptyList())
    val kalendarEvents = schedulesFromDb.map { schedule ->
        KalendarEvent(
            date = LocalDate.parse(schedule.date.toString()),
            eventName = "Employee ID: ${schedule.employeeId}, Shift Type: ${ShiftType.entries[schedule.shiftTypeId]}"
        )
    }

    var clickedDay = remember { mutableStateOf<LocalDate?>(null) }
    var eventsOnClickedDay = remember { mutableStateOf<List<KalendarEvent>>(emptyList()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            KalendarView(kalendarEvents, clickedDay, eventsOnClickedDay)
            EventList(clickedDay.value, eventsOnClickedDay.value)
        }
    }
}


@Composable
fun KalendarView(
    kalendarEvents: List<KalendarEvent>,
    clickedDay: MutableState<LocalDate?>,
    eventsOnClickedDay: MutableState<List<KalendarEvent>>
) {
    Kalendar(
        currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        kalendarType = KalendarType.Firey,
//            modifier = Modifier.weight(1f),
        events = KalendarEvents(events = kalendarEvents),
        onDayClick = { day, eventsOnDay ->
            clickedDay.value = day
            eventsOnClickedDay.value = eventsOnDay
        }
    )
}

@Composable
fun EventList(clickedDay: LocalDate?, events: List<KalendarEvent>) {
    if (clickedDay != null) {
        Text("Events on $clickedDay")
        val filteredEvents = events.filter { it.date == clickedDay }
        LazyColumn {
            items(filteredEvents.size) { index ->
                EventCard(filteredEvents[index])
            }
        }
    }
}

@Composable
fun EventCard(event: KalendarEvent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Event: ${event.eventName}")
    }
}
