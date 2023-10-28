package com.example.f23hopper.ui.shiftedit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compose.CustomColor
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.maxShifts
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.rememberLockOpen
import com.example.f23hopper.utils.CalendarUtilities.InvalidDayIcon
import com.example.f23hopper.utils.ShiftCircles
import com.example.f23hopper.utils.ShiftIcon
import com.example.f23hopper.utils.isWeekday
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ShiftEditScreen(
    clickedDay: LocalDate,
    navController: NavController,
    viewModel: ShiftEditViewModel = hiltViewModel()
) {
    val shiftsFlow: Flow<List<Shift>> = viewModel.getShiftsForDay(clickedDay)
    val shifts by shiftsFlow.collectAsState(initial = emptyList())
    val groupedShifts = shifts.groupBy { ShiftType.values()[it.schedule.shiftTypeId] }

    var isSpecialDay by remember { mutableStateOf(false) } // check the table to be sure
    LaunchedEffect(clickedDay) {
        isSpecialDay = viewModel.isSpecialDay(clickedDay)
    }

    val context =
        ShiftContext(
            viewModel,
            shiftsOnDay = groupedShifts,
            date = clickedDay,
            isSpecialDay = isSpecialDay
        )
    Column(modifier = Modifier.fillMaxWidth()) {
        DateHeader(context, navController)
        DisplayShifts(context)
    }
}


@Composable
fun DateHeader(context: ShiftContext, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back button
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clickable {
                    // logic for cancel here, currently just goes back.
                    navController.popBackStack()
                }
                .size(30.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            val date = context.date

            // Date
            Text(
                text = "${
                    date.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                } ${date.dayOfMonth} ${date.year}",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            InvalidDayIcon(
                context.shiftsOnDay,
                context.date.toJavaLocalDate(),
                context.isSpecialDay,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(30.dp, 30.dp),
                showDialogueOnClick = true
            )
        }

        // empty space to ensure center alignment of the text
        Spacer(modifier = Modifier.size(30.dp))
    }
}

@Composable
fun DisplayShifts(
    context: ShiftContext
) {
    LazyColumn {
        val rowCount = maxShifts(context.isSpecialDay)
        if (context.date.isWeekday()) {
            addShiftTypeSection(
                context,
                ShiftType.DAY,
                rowCount
            )
            addShiftTypeSection(context, ShiftType.NIGHT, rowCount)
        } else {
            addShiftTypeSection(context, ShiftType.FULL, rowCount)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.addShiftTypeSection(
    context: ShiftContext,
    shiftType: ShiftType,
    rowCount: Int,
) {
    stickyHeader {
        ShiftTypeHeader(shiftType = shiftType, context = context)
    }
    val shifts = context.shiftsOnDay[shiftType] ?: emptyList()
    for (i in 0 until rowCount) {
        if (i < shifts.size) {
            item { FilledShiftRow(context.viewModel, shifts[i]) }

        } else {
            item { EmptyShiftRow(context.viewModel, shiftType, context.date) }
        }
    }
}


@Composable
fun EmployeeDropdown(
    modifier: Modifier,
    employees: List<Employee>,
    onEmployeeSelected: (Employee) -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
            onDismiss()
        },
        modifier = modifier
    ) {
        employees.forEach { employee ->
            DropdownMenuItem(
                interactionSource = interactionSource,
                onClick = {
                    onEmployeeSelected(employee)
                    expanded = false
                },

                text = {
                    Text(
                        text = "${employee.firstName} ${employee.lastName}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
    }
}


@Composable
fun ShiftTypeHeader(shiftType: ShiftType, context: ShiftContext) {
    val shiftLabel = when (shiftType) {
        ShiftType.DAY -> "Day Shift"
        ShiftType.NIGHT -> "Night Shift"
        ShiftType.FULL -> "Full Shift"
        else -> {
            ""
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CustomColor.secondaryBackground)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShiftIcon(shiftType)
            ShiftCircles(
                maxShifts = maxShifts(context.isSpecialDay),
                shiftCount = context.shiftsOnDay[shiftType]?.size ?: 0,
                shiftType = shiftType
            )
            Text(text = shiftLabel, style = MaterialTheme.typography.titleLarge)
        }
    }

    Divider(
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}


@Composable
fun FilledShiftRow(
    viewModel: ShiftEditViewModel,
    shift: Shift
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.AccountBox,
            contentDescription = "Shift Icon",
            modifier = Modifier.size(30.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .width(IntrinsicSize.Min)
        ) {
            // Employee Info
            EmployeeText(shift)
        }

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Shift",
            modifier = Modifier
                .size(24.dp)
                .clickable { viewModel.deleteShift(shift.schedule) }
        )
    }
}

@Composable
fun EmployeeText(shift: Shift) {
    Text(
        text = "${shift.employee.firstName} ${shift.employee.lastName}",
        style = MaterialTheme.typography.headlineSmall
    )
    //  Display appropriate canOpen/canClose tags
    if (shift.employee.canOpen && shift.schedule.shiftTypeId != ShiftType.NIGHT.ordinal) {
        CanOpenIcon(text = true)
    }

    if (shift.employee.canClose && shift.schedule.shiftTypeId != ShiftType.DAY.ordinal) {
        CanCloseIcon(text = true)
    }
}

@Composable
fun EmptyShiftRow(viewModel: ShiftEditViewModel, shiftType: ShiftType, date: LocalDate) {
    var showEmployeeList by remember { mutableStateOf(false) }
    val employeesFlow = viewModel.getEligibleEmployeesForShift(date, shiftType)
    val employees by employeesFlow.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .clickable { showEmployeeList = !showEmployeeList }
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Shift Icon",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "Add Employee",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.LightGray
                )
                Icon(
                    imageVector = if (showEmployeeList) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = "Add Employee",
                    modifier = Modifier.size(30.dp)
                )
            }

            // Animate the list moving down
            AnimatedVisibility(visible = showEmployeeList) {
                Column(modifier = Modifier.animateContentSize()) {
                    EmployeeList(
                        date = date.toJavaLocalDate(),
                        employees = employees,
                        shiftType = shiftType,
                        viewModel = viewModel
                    ) { employee ->
                        viewModel.addEmployeeToShift(employee, shiftType, date)
                        showEmployeeList = false
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeList(
    date: java.time.LocalDate,
    employees: List<Employee>,
    shiftType: ShiftType,
    viewModel: ShiftEditViewModel,
    onEmployeeClick: (Employee) -> Unit,
) {
    val shiftsThisWeekMap by viewModel.getShiftCountsForWeek(date)
        .collectAsState(initial = emptyMap())

    employees.forEach { employee ->
        val shiftsThisWeek = shiftsThisWeekMap[employee.employeeId] ?: 0

        Row(
            modifier = Modifier
                .clickable { onEmployeeClick(employee) }
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = "${employee.firstName} ${employee.lastName} - Shifts: $shiftsThisWeek ",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (employee.canOpen && shiftType != ShiftType.NIGHT) {
                        CanOpenIcon()
                    }

                    if (employee.canClose && shiftType != ShiftType.DAY) {
                        CanCloseIcon()
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add ${employee.firstName} ${employee.lastName}",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun CanOpenIcon(text: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = rememberLockOpen(),
            contentDescription = "Can Open",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        if (text) {
            Text(
                text = "Can Open",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun CanCloseIcon(text: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = rememberLock(),
            contentDescription = "Can Close",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        if (text) {
            Text(
                text = "Can Close",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            )
        }
    }
}

