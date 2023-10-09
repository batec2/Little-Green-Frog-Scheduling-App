package com.example.f23hopper.ui.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.ScheduleWithEmployee
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import com.example.f23hopper.utils.displayText
import com.example.f23hopper.utils.rememberFirstCompletelyVisibleMonth
import com.example.f23hopper.utils.toKotlinxLocalDate
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class Event(val date: LocalDate, val eventName: String, val shiftType: ShiftType)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CalendarScreen(navigateToDayView: (String) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val clickedDay = remember { mutableStateOf<LocalDate?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val viewModel = hiltViewModel<CalendarSchedulesViewModel>()
    val events by viewModel.parsedEvents.collectAsState(initial = emptyList())


    clickedDay.value = LocalDate.of(2023, 10, 7)
    Calendar(events) { day ->
        clickedDay.value = LocalDate.parse(day)
        coroutineScope.launch {
            sheetState.expand()
        }
    }

//    EventDetailsBottomSheet(sheetState, clickedDay)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsBottomSheet(sheetState: SheetState, clickedDay: MutableState<LocalDate?>) {
    if (sheetState.isVisible) {
        ModalBottomSheet(onDismissRequest = { /* Handle dismiss */ },
            sheetState = sheetState,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                ) {
                    clickedDay.value?.let {
                        WeekViewScreen(clickedDay.value!!.toKotlinxLocalDate())
                    }
                }
            })
    }
}

@Composable
fun Calendar(events: List<ScheduleWithEmployee>, navigateToDayView: (String) -> Unit) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }

    val eventsOnSelectedDate = if (selection?.date != null) {
        events.filter { it.schedule.date.toString() == selection?.date.toString() }
    } else {
        emptyList()
    }


    val eventsByDay =
        events.groupBy { it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }
    val colorsForDays = getColorsForDays(eventsByDay)


    //TODO: Fix stuttering of top bar
    StatusBarColorUpdateEffect(toolbarColor)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundColor),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek().first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

        LaunchedEffect(visibleMonth) {
            selection = null
        }

        SimpleCalendarTitle(
            modifier = Modifier
                .background(toolbarColor)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )
        HorizontalCalendar(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            dayContent = { day ->
                val dayColors = colorsForDays[day.date] ?: emptyList()
                Day(day = day, isSelected = selection == day, colors = dayColors) { clicked ->
                    selection = clicked
//                    navigateToDayView(clicked.date.toString())
                    Log.d("Test", clicked.date.toString())
                }
            },
            monthHeader = {
                WeekDays(modifier = Modifier.padding(vertical = 8.dp))
            },
        )
//        Divider(color = itemBackgroundColor)
        if (selection != null) {  // Conditionally render based on selection
            Divider(color = itemBackgroundColor)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = eventsOnSelectedDate) { event ->
                    EmployeeInfoForDay(event)
                }
            }
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box( // Square days!!
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) selectedItemColor else Color.Transparent
            )
            .padding(1.dp)
            .background(color = itemBackgroundColor)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) })
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> MaterialTheme.colorScheme.onBackground
            DayPosition.InDate,
            DayPosition.OutDate -> inActiveTextColor // Grey out days not in current month
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp
        )

        ColorGroupLayout(colors = colors, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ColorGroupLayout(colors: List<Color>, modifier: Modifier = Modifier) {
    val groupedColors = colors.groupBy { it }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        groupedColors.forEach { (_, colorList) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                colorList.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekDays(modifier: Modifier) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek()) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun LazyItemScope.EmployeeInfoForDay(
    event: ScheduleWithEmployee
) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        val dateIcon = Icons.Outlined.DateRange
        Box(
            modifier = Modifier
                .background(color = itemBackgroundColor)
                .fillParentMaxWidth(1 / 7f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Icon(dateIcon, contentDescription = "Date Icon")
//            Text(
//                text = event.schedule.date.toString(),
//                textAlign = TextAlign.Center,
//                lineHeight = 17.sp,
//                fontSize = 12.sp,
//            )
        }
        Box(
            modifier = Modifier
                .background(color = itemBackgroundColor)
                .weight(1f)
                .fillMaxHeight(),
        ) {
            EmployeeInformation(event.employee)
        }
    }
    Divider(color = pageBackgroundColor, thickness = 2.dp)
}

@Composable
private fun EmployeeInformation(employee: Employee) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        val icon =
            Icons.Outlined.Person  // Assuming you want to use a person icon for employees
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(icon, contentDescription = "Employee Icon")
        }
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = employee.firstName,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${employee.firstName} ${employee.lastName}",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}


val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val selectedItemColor: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val inActiveTextColor: Color @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

@Composable
fun getColorsForDays(eventsByDay: Map<LocalDate, List<ScheduleWithEmployee>>): Map<LocalDate, List<Color>> {
    val isDarkTheme = isSystemInDarkTheme()

    return eventsByDay.mapValues { entry ->
        entry.value.map { event ->
            when (ShiftType.values()[event.schedule.shiftTypeId]) {
                ShiftType.CANT_WORK -> MaterialTheme.colorScheme.primary
                ShiftType.DAY -> if (isDarkTheme) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.primary
                ShiftType.NIGHT -> if (isDarkTheme) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.onTertiaryContainer
                ShiftType.FULL -> MaterialTheme.colorScheme.secondary
                else -> Color.Transparent
            }
        }
    }
}
