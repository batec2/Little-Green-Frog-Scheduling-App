package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.maxShifts
import kotlinx.coroutines.launch

@Composable
fun ToggleSpecialDayButton(
    toggleSpecialDay: suspend () -> Unit,
    isSpecialDay: Boolean,
    modifier: Modifier = Modifier,
    shiftsOnDay: Map<ShiftType, List<Shift>>,
    iconSize: Dp = (28.dp)

) {
    val coroutineScope = rememberCoroutineScope()

    // add shift types to a list if they max out shifts on special days
    val maxedOutShiftTypes = remember(shiftsOnDay, isSpecialDay) {
        shiftsOnDay.entries.filter { (_, shifts) ->
            shifts.size >= maxShifts(isSpecialDay)
        }.map { it.key }
    }

    val (showAlert, setShowAlert) = remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = {
            if (isSpecialDay && maxedOutShiftTypes.isNotEmpty()) {
                setShowAlert(true)
            } else {
                coroutineScope.launch {
                    toggleSpecialDay()
                }
            }
        }
    ) {
        if (!isSpecialDay) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Not Special Day",
                modifier.size(iconSize)
            )
        } else
            Icon(
                imageVector = Icons.Filled.Star, contentDescription = "Special Day",
                modifier.size(iconSize)
            )
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { setShowAlert(false) },
            title = { Text(text = "Max Shifts Reached") },
            text = {
                Text(
                    "Remove a shift from the following type(s) before toggling off Busy Day: " +
                            maxedOutShiftTypes.joinToString(", ") { it.name }
                )
            },
            confirmButton = {
                Button(onClick = { setShowAlert(false) }) {
                    Text("OK")
                }
            }
        )
    }
}
