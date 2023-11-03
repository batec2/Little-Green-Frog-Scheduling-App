package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.getShiftIcon
import com.example.f23hopper.utils.isWeekday
import com.example.f23hopper.utils.maxShiftRows
import com.example.f23hopper.utils.maxShifts
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.flow.filter
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarPager(
    modifier: Modifier = Modifier,
    selection: CalendarDay?,
    shiftsOnSelectedDate: Map<ShiftType, List<Shift>>,
    specialDaysByDay: Map<LocalDate, List<SpecialDay>>,
    navigateToShiftView: (String) -> Unit,
    toggleSpecialDay: suspend () -> Unit,
    viewModel: CalendarViewModel,
    employee: (Employee) -> Unit,//passes employeeId to Calendar
    employees: List<ViewItem>,//list ids for employees selected for schedule view
    employeeList: List<Employee>,
    clearList:()->Unit
) {
    Column(
        modifier = modifier
    ) {
        //List of current selected employees
        val pagerState = rememberPagerState(initialPage = 0)
        HorizontalPager(pageCount = 2, state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when(page) {
                0->//shows employees scheduled to work on current day
                if (selection != null) {
                    //Divider(color = itemBackgroundColor)
                    val isSpecialDay = specialDaysByDay[selection?.date!!] != null
                    ShiftDetailsForPagerDay(
                        shiftsOnSelectedDay = shiftsOnSelectedDate,
                        selection.date,
                        isSpecialDay = isSpecialDay,
                        navigateToShiftView,
                        toggleSpecialDay,
                        viewModel = viewModel,
                        employee = employee
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(text = "Select Day")
                    }
                }
                1->//Shows employees selected for the employee shift view
                ShiftViewPage(
                    employees = employees
                    , employeeList = employeeList
                    , clearList = clearList)
            }

        }
        Row(
            Modifier
                //.wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .weight(0.1f),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(2) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(5.dp)
                )
            }
        }
    }
}

@Composable
fun ShiftViewPage(
    employees: List<ViewItem>,
    employeeList: List<Employee>,
    clearList:()->Unit
){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically

    ) {
        if(employees.isEmpty()){
            Text(text = "No Employees Selected for Shift View")
        }
        else{
            //Checks if employee id is in the list of selected employees for shift view
            employees.forEach{item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            start = 2.dp,
                            end = 2.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .background(item.color.colVal),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = item.empItem.firstName)
                    Text(text = item.empItem.lastName)
                }
            }

            //Clears list of employees for shift view
            //Temporary replace later
            Button(onClick = (clearList)) {
                Text(text = "Clear")
            }
        }
    }
}

@Composable
fun ShiftDetailsForPagerDay(
    shiftsOnSelectedDay: Map<ShiftType, List<Shift>>,
    date: LocalDate,
    isSpecialDay: Boolean = false,
    navigateToShiftView: (String) -> Unit,
    toggleSpecialDay: suspend () -> Unit,
    viewModel: CalendarViewModel,
    employee: (Employee) -> Unit//passes employeeId to next composable
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        ShiftContent(
            date = date,
            shifts = shiftsOnSelectedDay,
            isSpecialDay = isSpecialDay,
            navigateToShiftView = navigateToShiftView,
            modifier = Modifier.weight(0.8f), // 80% of the total width
            viewModel = viewModel,
            employee = employee
        )
        Divider(
            color = Color.Gray, modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
        )
        CalendarPagerActionBox(
            date = date,
            isSpecialDay = isSpecialDay,
            navigateToShiftView = navigateToShiftView,
            toggleSpecialDay = toggleSpecialDay,
            shiftsOnDay = shiftsOnSelectedDay,
            modifier = Modifier.weight(0.1f) // 80% of the total width
        )
    }
}

@Composable
fun CalendarPagerActionBox(
    date: LocalDate,
    navigateToShiftView: (String) -> Unit,
    toggleSpecialDay: suspend () -> Unit,
    isSpecialDay: Boolean,
    modifier: Modifier,
    shiftsOnDay: Map<ShiftType, List<Shift>>,
) {
    Box(
        modifier = modifier
            .width(80.dp)
            .height(2 * 55.dp)
            .background(pageBackgroundColor)
            .clickable {
                navigateToShiftView(date.toString())
            }, contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditShiftButton(onClick = {

                navigateToShiftView(date.toString())
            })
            ToggleSpecialDayButton(
                toggleSpecialDay = { toggleSpecialDay() },
                isSpecialDay = isSpecialDay,
                shiftsOnDay = shiftsOnDay,
            )
        }
    }
}

@Composable
fun ShiftContent(
    date: LocalDate,
    shifts: Map<ShiftType, List<Shift>>,
    isSpecialDay: Boolean = false,
    navigateToShiftView: (String) -> Unit,
    modifier: Modifier,
    viewModel: CalendarViewModel,
    employee: (Employee) -> Unit //passes employeeId to next composable
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(pageBackgroundColor)
    ) {
        // dots in each shift type

        // Build 2 rows if weekday, 1 row if weekend
        if (date.isWeekday()) {
            ShiftRow(
                shiftType = ShiftType.DAY,
                shiftsForType = shifts[ShiftType.DAY].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),// divide by amt of rows
                maxShifts = maxShifts(isSpecialDay),
                viewModel = viewModel,
                employee = employee
            )
            /*
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outline)
            )
             */
            ShiftRow(
                shiftType = ShiftType.NIGHT,
                shiftsForType = shifts[ShiftType.NIGHT].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                maxShifts = maxShifts(isSpecialDay),
                viewModel = viewModel,
                employee = employee
            )
        } else {
            ShiftRow(
                shiftType = ShiftType.FULL,
                shiftsForType = shifts[ShiftType.FULL].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                maxShifts = maxShifts(isSpecialDay),
                viewModel = viewModel,
                employee = employee
            )

        }
    }

}

@Composable
fun ShiftRow(
    maxShifts: Int,
    shiftType: ShiftType,
    shiftsForType: List<Shift>,
    date: LocalDate,
    navigateToShiftView: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    employee: (Employee) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        shiftsForType.forEach { shift ->
            ShiftRowEmployeeEntry(
                shift = shift,
                onEmployeeClick = { employeeId ->
                    employee(employeeId)
                },
                modifier = Modifier.weight(1f)
            )
        }
        if (shiftsForType.size < maxShifts) {
            for (i in 1..(maxShifts - shiftsForType.size)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(2.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            navigateToShiftView(date.toString())
                        }
                ) {
                    // Empty row
                }
            }
        }
    }
}

@Composable
fun ShiftRowEmployeeEntry(
    shift: Shift,
    onEmployeeClick: (Employee) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onEmployeeClick(shift.employee) }
            .background(
                if(shift.schedule.shiftType == ShiftType.DAY) {
                    if(isSystemInDarkTheme()) {
                        Color(0xFF72B9E0) //Day Dark
                    } else {
                        Color(0xFFB1C1F2) //Day Light
                    }
                } else {
                    if(isSystemInDarkTheme()) {
                        Color(0xFF2C8D76) //Evening Dark
                    } else {
                        Color(0xFFA18AB4) //Evening Light
                    }
                }
            )
            .clickable { onEmployeeClick(shift.employee) },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = getShiftIcon(shiftType = shift.schedule.shiftType),
            contentDescription = "shift icon",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(30.dp) // size of the icon
        )
        Spacer(Modifier.width(10.dp)) // add spacing between icon and text
        Text(
            text = shift.employee.firstName + " " + shift.employee.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(IntrinsicSize.Max)
        )

    }
}

@Composable
fun ShiftCompletionText(
    shiftsForType: List<Shift>,
    shiftType: ShiftType,
    maxShifts: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = if (shiftsForType.size >= maxShifts) "$shiftType Shift Covered" else "Incomplete",
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier
    )
}

@Composable
fun EditShiftButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Add Employee")
    }
}
