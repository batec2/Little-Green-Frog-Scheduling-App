package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import com.example.f23hopper.utils.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

data class ViewItem(
    val empItem: Employee,
    val color: ShiftViewColors,
)

@Composable
fun Calendar(
    calendarContext: CalendarContext,
) {
    val context = LocalContext.current
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(10) }
    val endMonth = remember { currentMonth.plusMonths(10) }
    val snackbarHostState = remember { SnackbarHostState() }
    val shiftsByDay = calendarContext.shifts.groupBy {
        it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
    val colors = listOf(
        ShiftViewColors.COLOR1,
        ShiftViewColors.COLOR2,
        ShiftViewColors.COLOR3,
        ShiftViewColors.COLOR4,
        ShiftViewColors.COLOR5,
        ShiftViewColors.COLOR6
    )

    // Holds employee id for shift highlighting
    val viewItemList = remember { mutableStateListOf<ViewItem>() }

    // all shifts dates for the current month for employees selected for shift view
    val employeeShifts = calendarContext.shifts.filter { shift ->
        (viewItemList.any { emp -> emp.empItem == shift.employee })
    }.map { it.schedule.date.toJavaLocalDate() }

    val shiftsOnSelectedDate = calendarContext.selection?.date?.let { selectedDate ->
        shiftsByDay[selectedDate]?.groupBy { it.schedule.shiftType }
    } ?: emptyMap()

    val specialDaysByDay = calendarContext.specialDays.groupBy {
        it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
    StatusBarColorUpdateEffect(toolbarColor)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundColor)
    ) {
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
                employees = calendarContext.employees,
                shifts = calendarContext.shifts,
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
                    calendarContext.viewModel.exportSchedule(
                        calendarContext.shifts,
                        currentMonth,
                        context,
                        onExportComplete = { message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
                }
            )

            CalendarBody(
                modifier = Modifier.wrapContentWidth(),
                calendarState = state,
                calendarContext = calendarContext,
                viewItemList = viewItemList
            )

            val calendarPagerContext = CalendarPagerContext(
                selection = calendarContext.selection,
                shiftsOnSelectedDate = shiftsOnSelectedDate,
                specialDaysByDay = specialDaysByDay,
                navigateToShiftView = calendarContext.navigateToShiftView,
                toggleSpecialDay = {
                    coroutineScope.launch {
                        calendarContext.viewModel.toggleSpecialDay(calendarContext.selection?.date?.toSqlDate())
                    }

                },
                //Employee selected limit is 6, if employee already in list it gets removed
                //else if the list is less than 6 entries then it gets added
                employeeAction = {
                    if (viewItemList.any { emp -> emp.empItem == it }) {
                        viewItemList.removeIf { emp -> emp.empItem == it }
                    } else if (viewItemList.size <= 6) {
                        viewItemList.add(
                            ViewItem(
                                empItem = it,
                                color = getColor(viewItemList, colors)
                            )
                        )
                    }
                },
                viewItemList = viewItemList,
                employeeList = calendarContext.employees,
                clearList = { viewItemList.clear() }
            )
            CalendarPager(
                modifier = Modifier.fillMaxSize(),
                calendarPagerContext,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


fun getColor(viewList: List<ViewItem>, colorList: List<ShiftViewColors>): ShiftViewColors {
    return if (viewList.isNotEmpty()) {
        (colorList.filter { item -> viewList.none { emp -> emp.color == item } }).first()
    } else {
        colorList.first()
    }
}

@Composable
fun CalendarBody(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    calendarContext: CalendarContext,
    viewItemList: List<ViewItem>
) {
    val shiftsByDay = calendarContext.shifts.groupBy {
        it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    // All shifts dates for the current month for employees selected for shift view
    val employeeShifts = calendarContext.shifts.filter { shift ->
        (viewItemList.any { emp -> emp.empItem == shift.employee })
    }.map { it.schedule.date.toJavaLocalDate() }

    val specialDaysByDay = calendarContext.specialDays.groupBy {
        it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    HorizontalCalendar(
        modifier = modifier,
        state = calendarState,
        dayContent = { day ->
            val employeeShiftSelected = employeeShifts.contains(day.date)
            val isSpecialDay = specialDaysByDay[day.date] != null
            val shiftsOnDay = shiftsByDay[day.date]?.groupBy { it.schedule.shiftType }.orEmpty()
            val isSelected = calendarContext.selection == day

            DayContext(
                viewModel = calendarContext.viewModel,
                day = day,
                shiftsOnDay = shiftsOnDay,
                isSpecialDay = isSpecialDay,
                isSelected = isSelected,
                employeeShiftSelected = employeeShiftSelected,
                viewItemList = viewItemList
            ).let { context ->
                Day(context) { clicked ->
                    calendarContext.onSelectionChanged(clicked)
                }
            }
        },
        monthHeader = {
            WeekDays(modifier = Modifier.padding(vertical = 8.dp))
        }
    )
}
