package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.f23hopper.utils.getShiftIcon
import com.example.f23hopper.utils.getShiftRowColor
import com.example.f23hopper.utils.isWeekday
import com.example.f23hopper.utils.maxShiftRows
import com.example.f23hopper.utils.maxShifts
import com.kizitonwose.calendar.core.CalendarDay
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
    viewItemList: List<ViewItem>,//list ids for employees selected for schedule view
    employeeList: List<Employee>,
    clearList: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        //List of current selected employees
        val pagerState = rememberPagerState(initialPage = 0)
        HorizontalPager(pageCount = 2, state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 ->//shows employees scheduled to work on current day
                    if (selection != null) {
                        val isSpecialDay = specialDaysByDay[selection?.date!!] != null
                        ShiftDetailsForPagerDay(
                            shiftsOnSelectedDay = shiftsOnSelectedDate,
                            selection.date,
                            isSpecialDay = isSpecialDay,
                            navigateToShiftView,
                            toggleSpecialDay,
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

                1 ->//Shows employees selected for the employee shift view
                    ShiftViewPage(
                        viewItemList = viewItemList,
                        employeeList = employeeList,
                        clearList = clearList
                    )
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
                    if (pagerState.currentPage == iteration) colorScheme.onSurface.copy(alpha = 0.8f)
                    else colorScheme.onSurface.copy(alpha = 0.3f)
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
    viewItemList: List<ViewItem>,
    employeeList: List<Employee>,
    clearList: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically

    ) {
        if (viewItemList.isEmpty()) {
            Text(text = "No Employees Selected for Shift View")
        } else {
            //Checks if employee id is in the list of selected employees for shift view
            viewItemList.forEach { item ->
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
                ) {
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
                date = date,
                shiftsForType = shifts[ShiftType.DAY].orEmpty(),
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),// divide by amt of rows
                maxShifts = maxShifts(isSpecialDay),
                employee = employee
            )
            ShiftRow(
                maxShifts = maxShifts(isSpecialDay),
                shiftsForType = shifts[ShiftType.NIGHT].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                employee = employee
            )
        } else {
            ShiftRow(
                maxShifts = maxShifts(isSpecialDay),
                shiftsForType = shifts[ShiftType.FULL].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                employee = employee
            )

        }
    }

}

@Composable
fun ShiftRow(
    maxShifts: Int,
    shiftsForType: List<Shift>,
    date: LocalDate,
    navigateToShiftView: (String) -> Unit,
    modifier: Modifier = Modifier,
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
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.4f))
                        .clickable {
                            navigateToShiftView(date.toString())
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Employee",
                        tint = colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier
                            .size(30.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Max)
                            .align(Alignment.CenterVertically)
                    )

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
            .background(getShiftRowColor(shiftType = shift.schedule.shiftType))
            .clickable { onEmployeeClick(shift.employee) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = getShiftIcon(shiftType = shift.schedule.shiftType),
            contentDescription = "shift icon",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(30.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.weight(.7f))
        Text(

            text = shift.employee.nickname.ifBlank { shift.employee.firstName + " " + shift.employee.lastName },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.weight(1f))
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
