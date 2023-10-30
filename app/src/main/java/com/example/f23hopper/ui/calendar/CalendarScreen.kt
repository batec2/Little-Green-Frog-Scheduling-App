@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.displayText
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek


@Composable
fun CalendarScreen(navigateToShiftView: (String) -> Unit) {
//    val coroutineScope = rememberCoroutineScope()
    var selection by rememberSaveable { mutableStateOf<CalendarDay?>(null) }

    val viewModel = hiltViewModel<CalendarViewModel>()
    val shifts by viewModel.shifts.collectAsState(initial = emptyList())
    val employees by viewModel.employees.collectAsState(initial = emptyList())
    val specialDays by viewModel.days.collectAsState(initial = emptyList())

    Calendar(
        shifts = shifts,
        employees = employees,
        specialDays = specialDays,
        navigateToShiftView,
        viewModel = viewModel,
        selection = selection,
        onSelectionChanged = { selection = it })
}


@Composable
fun WeekDays(modifier: Modifier) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek()) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}


@Composable
fun getShiftColor(shiftType: ShiftType): Color {
    val isDarkTheme = isSystemInDarkTheme()
    return when (shiftType) {
        ShiftType.CANT_WORK -> MaterialTheme.colorScheme.primary
        ShiftType.DAY -> if (isDarkTheme) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.primary
        ShiftType.NIGHT -> if (isDarkTheme) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.onTertiaryContainer
        ShiftType.FULL -> MaterialTheme.colorScheme.secondary
        else -> Color.Transparent
    }
}

@Composable
fun generateGroupedColors(shiftsOnDay: Map<ShiftType, List<Shift>>): Map<Color, List<Color>> {
    // Map all shifts to their corresponding colors

    val shiftTypeToColor = shiftsOnDay.keys.associateWith { getShiftColor(it) }
    val allColors = shiftsOnDay.flatMap { entry ->
        val colorForShiftType = shiftTypeToColor[entry.key] ?: Color.Transparent
        entry.value.map { colorForShiftType }
    }

    // Group these colors
    return allColors.groupBy { it }
}

