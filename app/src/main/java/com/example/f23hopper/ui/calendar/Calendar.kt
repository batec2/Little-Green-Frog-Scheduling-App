package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import com.example.f23hopper.utils.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

@Composable
fun Calendar(
    shifts: List<Shift>,
    employees: List<Employee>,
    specialDays: List<SpecialDay>,
    navigateToShiftView: (String) -> Unit,
    viewModel: CalendarViewModel,
    selection: CalendarDay?,
    onSelectionChanged: (CalendarDay?) -> Unit
) {
    val context = LocalContext.current
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(10) }
    val endMonth = remember { currentMonth.plusMonths(10) }

    val shiftsByDay =
        shifts.groupBy { it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }


    val shiftsOnSelectedDate = selection?.date?.let { selectedDate ->
        shiftsByDay[selectedDate]?.groupBy { ShiftType.values()[it.schedule.shiftTypeId] }
    } ?: emptyMap()

    val specialDaysByDay =
        specialDays.groupBy { it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }

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


        CalendarTitle(
            employees = employees,
            shifts = shifts,
            modifier = Modifier
                .background(toolbarColor)
                .padding(horizontal = 8.dp, vertical = 0.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch { state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth) }
            },
            onExportClick = {
//                           Log.d("emps:",viewModel.EmpsAsStrings()) */
            }
        )

        CalendarBody(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            shiftsByDay = shiftsByDay,
            specialDaysByDay = specialDaysByDay,
            selection = selection,
            onSelectionChanged = onSelectionChanged,
            viewModel = viewModel
        )

        CalendarPager(
            modifier = Modifier.fillMaxSize(),
            selection = selection,
            shiftsOnSelectedDate = shiftsOnSelectedDate,
            specialDaysByDay = specialDaysByDay,
            navigateToShiftView = navigateToShiftView
        )
    }
}


@Composable
fun CalendarBody(
    modifier: Modifier = Modifier,
    state: CalendarState,
    shiftsByDay: Map<LocalDate, List<Shift>>,
    specialDaysByDay: Map<LocalDate, List<SpecialDay>>,
    selection: CalendarDay?,
    onSelectionChanged: (CalendarDay?) -> Unit,
    viewModel: CalendarViewModel
) {
    HorizontalCalendar(
        modifier = modifier,
        state = state,
        dayContent = { day ->
            val isSpecialDay = specialDaysByDay[day.date] != null
            val context = DayContext(
                viewModel = viewModel, day = day,
                shiftsOnDay = shiftsByDay[day.date]?.groupBy { ShiftType.values()[it.schedule.shiftTypeId] }
                    .orEmpty(),
                isSpecialDay = isSpecialDay,
                isSelected = selection == day
            )
            Day(
                context,
            ) { clicked ->
                onSelectionChanged(clicked)
//                    navigateToDayView(clicked.date.toString())
            }
        },
        monthHeader = {
            WeekDays(modifier = Modifier.padding(vertical = 8.dp))
        }
    )
}

