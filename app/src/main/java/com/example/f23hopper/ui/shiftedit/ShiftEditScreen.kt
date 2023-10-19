package com.example.f23hopper.ui.shiftedit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compose.CustomColor
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
        DisplayShifts(groupedShifts, clickedDay, isSpecialDay)
    }
}

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
            imageVector = Icons.Default.Close,
            contentDescription = "Cancel",
            modifier = Modifier
                .clickable {
                    // logic for cancel here, currently just goes back.
                    navController.popBackStack()
                }
                .size(30.dp),
        )

        // Date
        Text(
            text = "${
                date.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${date.dayOfMonth} ${date.year}",
            style = MaterialTheme.typography.headlineSmall
        )

        // Confirm button
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Confirm",
            modifier = Modifier
                .clickable {
                    // logic for cancel here
                    navController.popBackStack()
                }
                .size(30.dp),
        )
    }
}

@Composable
fun DisplayShifts(
    groupedShifts: Map<ShiftType, List<Shift>>,
    clickedDay: LocalDate,
    isSpecialDay: Boolean
) {
    LazyColumn {
        if (clickedDay.isWeekday()) {
            addShiftTypeSection(groupedShifts, ShiftType.DAY, isSpecialDay)
            addShiftTypeSection(groupedShifts, ShiftType.NIGHT, isSpecialDay)
        } else {
            addShiftTypeSection(groupedShifts, ShiftType.FULL, isSpecialDay)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.addShiftTypeSection(
    groupedShifts: Map<ShiftType, List<Shift>>,
    shiftType: ShiftType,
    isSpecialDay: Boolean
) {
    groupedShifts[shiftType]?.let {
        stickyHeader {
            ShiftTypeHeader(shiftType, it.size, isSpecialDay)
        }
        items(it) { shift ->
            ShiftItem(shift)
        }
    }
}

@Composable
fun ShiftTypeHeader(shiftType: ShiftType, shiftCount: Int, isSpecialDay: Boolean) {
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
                maxShifts = maxShifts(isSpecialDay),
                shiftCount = shiftCount,
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

@Composable
fun ShiftItem(shift: Shift) {
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
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Shift",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun EmployeeText(shift: Shift) {
    Text(
        text = "${shift.employee.firstName} ${shift.employee.lastName}",
        style = MaterialTheme.typography.headlineSmall
    )
    //  Display appropriate canopen/canClose tags when appropriate
    if (shift.employee.canOpen && shift.schedule.shiftTypeId != ShiftType.NIGHT.ordinal) {
        CanOpenIcon()
    }

    if (shift.employee.canClose) {
        CanCloseIcon()
    }
}

