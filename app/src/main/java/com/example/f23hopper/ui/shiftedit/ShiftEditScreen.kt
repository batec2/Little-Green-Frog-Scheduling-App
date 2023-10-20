package com.example.f23hopper.ui.shiftedit

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
import com.example.f23hopper.utils.ShiftCircles
import com.example.f23hopper.utils.ShiftIcon
import com.example.f23hopper.utils.isWeekday
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
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

    Column(modifier = Modifier.fillMaxWidth()) {
        DateHeader(clickedDay, navController)
        DisplayShifts(
            ShiftContext(
                viewModel,
                groupedShifts = groupedShifts,
                date = clickedDay,
                isSpecialDay = isSpecialDay
            )
        )
    }
}

data class ShiftContext(
    val viewModel: ShiftEditViewModel,
    val date: LocalDate,
    val groupedShifts: Map<ShiftType, List<Shift>>,
    val isSpecialDay: Boolean
)

@Composable
fun DateHeader(date: LocalDate, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Cancel button
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clickable {
                    // logic for cancel here, currently just goes back.
                    navController.popBackStack()
                }
                .size(30.dp),
        )

        Spacer(modifier = Modifier.weight(54 / 100f))

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
            modifier = Modifier.weight(4f)
        )

        Spacer(modifier = Modifier.weight(1f))

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
        ShiftTypeHeader(shiftType = shiftType, shiftCount = rowCount, context = context)
    }
    val shifts = context.groupedShifts[shiftType] ?: emptyList()
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
    anchor: Modifier,
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
        modifier = anchor
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
fun ShiftTypeHeader(shiftType: ShiftType, shiftCount: Int, context: ShiftContext) {
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
                shiftCount = context.groupedShifts[shiftType]?.size ?: 0,
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
    //  Display appropriate canOpen/canClose tags when appropriate
    if (shift.employee.canOpen && shift.schedule.shiftTypeId != ShiftType.NIGHT.ordinal) {
        CanOpenIcon()
    }

    if (shift.employee.canClose) {
        CanCloseIcon()
    }
}

@Composable
fun EmptyShiftRow(viewModel: ShiftEditViewModel, shiftType: ShiftType, date: LocalDate) {
    var showDropdown by remember { mutableStateOf(false) }
    val employeesFlow = viewModel.getEligibleEmployeesForShift(date, shiftType)
    val employees by employeesFlow.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { showDropdown = !showDropdown }
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
                imageVector = Icons.Default.Add,
                contentDescription = "Add Employee",
                modifier = Modifier.size(30.dp)
            )
        }

        if (showDropdown) {
            EmployeeDropdown(
                anchor = Modifier.align(Alignment.BottomCenter),
                employees = employees,
                onEmployeeSelected = { employee ->
                    viewModel.addEmployeeToShift(employee, shiftType, date)
                    showDropdown = false
                },
                onDismiss = {
                    showDropdown = false
                }
            )
        }
    }
}


@Composable
fun CanOpenIcon() {
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
        Text(
            text = "Can Open",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun CanCloseIcon() {
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
        Text(
            text = "Can Close",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )
    }
}

