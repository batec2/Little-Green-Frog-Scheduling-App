package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toKotlinxLocalDate
import com.example.f23hopper.utils.CalendarUtilities.toSqlDate
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
import java.util.Date

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

    //Holds employee id for shift highlighting
    val employee = remember { mutableStateListOf<Long>() }
    /*all shifts dates for the current montt for employees selected for shift view*/
    val employeeShifts = shifts.filter { shift -> employee.contains(shift.employee.employeeId)}
                .map { it.schedule.date.toJavaLocalDate() }

    val shiftsOnSelectedDate = selection?.date?.let { selectedDate ->
        shiftsByDay[selectedDate]?.groupBy { it.schedule.shiftType }
        } ?: emptyMap()

    shifts.groupBy { it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }

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
                viewModel.exportToCsv(shifts, currentMonth, context)
            }
        )

        CalendarBody(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            shiftsByDay = shiftsByDay,
            specialDaysByDay = specialDaysByDay,
            selection = selection,
            onSelectionChanged =
            {
                onSelectionChanged(it)
                //clears current selection if new day is selected
                //employee.longValue = -1
            }
            ,
            viewModel = viewModel,
            employeeShift = employeeShifts,
            employeesSelected = employee
        )

        CalendarPager(
            modifier = Modifier.fillMaxSize(),
            selection = selection,
            shiftsOnSelectedDate = shiftsOnSelectedDate,
            specialDaysByDay = specialDaysByDay,
            navigateToShiftView = navigateToShiftView,
            toggleSpecialDay = { viewModel.toggleSpecialDay(selection?.date?.toSqlDate()) },
            viewModel = viewModel,
            //Employee selected limit is 6, if employee already in list it gets removed
            //else if the list is less than 6 entries then it gets added
            employee = { if (employee.contains(it)) employee.remove(it)
                        else if(employee.size<=6) employee.add(it) },
            employees = employee,
            employeeList = employees,
            clearList = {
                //selected.clear()
                //Clears list of employee ids for shiftview
                employee.clear()
            }
        )
    }
}


@Composable
fun CalendarBody(
    modifier: Modifier = Modifier,
    state: CalendarState,
    shiftsByDay: Map<LocalDate, List<Shift>>,
    specialDaysByDay: Map<LocalDate, List<SpecialDay>>,
    employeeShift: List<LocalDate>,
    selection: CalendarDay?,
    onSelectionChanged: (CalendarDay?) -> Unit,
    viewModel: CalendarViewModel,
    employeesSelected: List<Long>
) {
    HorizontalCalendar(
        modifier = modifier,
        state = state,
        dayContent = { day ->
            val employeeShiftSelected = employeeShift.contains(day.date)
            val isSpecialDay = specialDaysByDay[day.date] != null
            val context = DayContext(
                viewModel = viewModel,
                day = day,
                shiftsOnDay = shiftsByDay[day.date]?.groupBy { it.schedule.shiftType }
                    .orEmpty(),
                isSpecialDay = isSpecialDay,
                isSelected = selection == day,
                employeeShiftSelected = employeeShiftSelected,
                employeesSelected = employeesSelected
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

