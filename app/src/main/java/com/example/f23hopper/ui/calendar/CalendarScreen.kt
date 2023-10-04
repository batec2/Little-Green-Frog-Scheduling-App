package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.shifttype.ShiftType
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
@ExperimentalMaterial3Api
fun CalendarScreen(
    navigateToDayView: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val clickedDay = remember { mutableStateOf<LocalDate?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val viewModel = hiltViewModel<CalendarSchedulesViewModel>()
    val schedulesFromDb by viewModel.schedules.collectAsState(initial = emptyList())
    val kalendarEvents = schedulesFromDb.map { schedule ->
        KalendarEvent(
            date = LocalDate.parse(schedule.date.toString()),
            eventName = "Employee ID: ${schedule.employeeId}, Shift Type: ${ShiftType.entries[schedule.shiftTypeId]}"
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            KalendarView(
                kalendarEvents = kalendarEvents,
                clickedDay = clickedDay,
                eventsOnClickedDay = remember { mutableStateOf(emptyList()) },
                navigateToDayView = { day ->
                    clickedDay.value = LocalDate.parse(day)
                    coroutineScope.launch {
                        sheetState.expand()
                    }
                }
            )
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { /* Handle dismiss */ },
            windowInsets = WindowInsets.systemBars,
            sheetState = sheetState,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                ) {
                    if (clickedDay.value != null) {
                        WeekViewScreen(clickedDay.value!!)
                    }
                }
            }
        )
    }
}

@Composable
fun KalendarView(
    kalendarEvents: List<KalendarEvent>,
    clickedDay: MutableState<LocalDate?>,
    eventsOnClickedDay: MutableState<List<KalendarEvent>>,
    navigateToDayView: (String) -> Unit
) {

    val kalendarColors = KalendarColors(
        color = List(12) {
            KalendarColor(
                backgroundColor = MaterialTheme.colorScheme.background,
                dayBackgroundColor = MaterialTheme.colorScheme.onBackground,
                headerTextColor = MaterialTheme.colorScheme.primary,
            )
        }
    )

    Kalendar(
        kalendarColors = kalendarColors,
        currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        kalendarType = KalendarType.Firey,
//            modifier = Modifier.weight(1f),
        events = KalendarEvents(events = kalendarEvents),
        onDayClick = { day, eventsOnDay ->
            clickedDay.value = day
            eventsOnClickedDay.value = eventsOnDay
            navigateToDayView(day.toString())
        }
    )
}

