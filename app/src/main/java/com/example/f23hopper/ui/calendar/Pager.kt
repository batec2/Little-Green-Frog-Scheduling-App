package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.utils.ShiftCircles
import com.example.f23hopper.utils.ShiftIcon
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
    navigateToShiftView: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        //        Divider(color = itemBackgroundColor)
        val pagerState = rememberPagerState(initialPage = 0)
        HorizontalPager(pageCount = 2, state = pagerState, modifier = Modifier.weight(1f)) { page ->
            if (page == 0) {
                if (selection != null) {
                    Divider(color = itemBackgroundColor)
                    val isSpecialDay = specialDaysByDay[selection?.date!!] != null
                    ShiftDetailsForPagerDay(
                        shiftsOnSelectedDate,
                        selection?.date!!,
                        isSpecialDay = isSpecialDay,
                        navigateToShiftView
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
            } else if (page == 1) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(text = "PUT STUFF HERE")
                }
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
fun ShiftDetailsForPagerDay(
    shifts: Map<ShiftType, List<Shift>>,
    date: LocalDate,
    isSpecialDay: Boolean = false,
    navigateToShiftView: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DateBox(date = date, navigateToShiftView = navigateToShiftView)
        ShiftContent(
            date = date,
            shifts = shifts,
            isSpecialDay = isSpecialDay,
            navigateToShiftView = navigateToShiftView
        )
    }
}

@Composable
fun DateBox(
    date: LocalDate,
    navigateToShiftView: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(2 * 55.dp)
            .background(pageBackgroundColor)
            .clickable {
                navigateToShiftView(date.toString())
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditShiftButton {
                navigateToShiftView(date.toString())
            }
//            Text(text = date.dayOfWeek.name.take(3).uppercase()) // Day of week abbreviated
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = date.dayOfMonth.toString()) // Day of the month
//            Spacer(
//                modifier = Modifier
//                    .height(1.dp)
//                    .background(Color.Black)
//            )
        }
    }
}

@Composable
fun ShiftContent(
    date: LocalDate,
    shifts: Map<ShiftType, List<Shift>>,
    isSpecialDay: Boolean = false,
    navigateToShiftView: (String) -> Unit
) {
    Column(
        modifier = Modifier
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
                maxShifts = maxShifts(isSpecialDay)
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outline)
            )
            ShiftRow(
                shiftType = ShiftType.NIGHT,
                shiftsForType = shifts[ShiftType.NIGHT].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                maxShifts = maxShifts(isSpecialDay)
            )
        } else {
            ShiftRow(
                shiftType = ShiftType.FULL,
                shiftsForType = shifts[ShiftType.FULL].orEmpty(),
                date = date,
                navigateToShiftView = navigateToShiftView,
                modifier = Modifier.weight(1f / maxShiftRows(date)),
                maxShifts = maxShifts(isSpecialDay)
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShiftIcon(shiftType)
        ShiftCircles(maxShifts, shiftsForType.size, shiftType)
        ShiftCompletionText(shiftsForType, shiftType, maxShifts)
        EditShiftButton {
            navigateToShiftView(date.toString())
        }
    }
}


@Composable
fun ShiftCompletionText(shiftsForType: List<Shift>, shiftType: ShiftType, maxShifts: Int) {
    Text(
        text = if (shiftsForType.size >= maxShifts) "$shiftType Shift Covered" else "Incomplete",
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Composable
fun EditShiftButton(navigateToShiftView: () -> Unit) {
    IconButton(onClick = navigateToShiftView) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Add Employee")
    }
}

