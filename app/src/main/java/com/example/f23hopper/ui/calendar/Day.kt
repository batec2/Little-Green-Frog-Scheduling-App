package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.CustomColor
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.utils.CalendarUtilities.InvalidDayIcon
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition

data class DayContext(
    val viewModel: CalendarViewModel,
    val day: CalendarDay,
    val shiftsOnDay: Map<ShiftType, List<Shift>>,
    val isSpecialDay: Boolean,
    val isSelected: Boolean,
)

@Composable
fun Day(
    context: DayContext,
    onClick: (CalendarDay) -> Unit = {},
) {
    val dayBackgroundColor = when {
        context.isSpecialDay -> CustomColor.specialDay
        // Todo: Look into a better color for dark off-month days.
        context.day.position != DayPosition.MonthDate -> if (isSystemInDarkTheme()) Color.Gray else MaterialTheme.colorScheme.tertiaryContainer
        else -> itemBackgroundColor
    }
    Box( // Square days!!
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = if (context.isSelected) 1.dp else 0.dp,
                color = if (context.isSelected) selectedItemColor else Color.Transparent
            )
            .padding(1.dp)
            .background(dayBackgroundColor)
            .clickable(
                enabled = true,/*day.position == DayPosition.MonthDate,*/
                onClick = { onClick(context.day) })
    ) {
        val textColor = when (context.day.position) {
            DayPosition.MonthDate -> MaterialTheme.colorScheme.onBackground
            DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor // Grey out days not in current month
        }


        InvalidDayIcon(
            context.shiftsOnDay,
            context.day.date,
            context.isSpecialDay,
            Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(15.dp, 15.dp)
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp, end = 4.dp),
            text = context.day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp
        )

        val groupedColors = generateGroupedColors(context.shiftsOnDay)
        ColorGroupLayout(groupedColors = groupedColors, modifier = Modifier.align(Alignment.Center))
    }
}


@Composable
fun ColorGroupLayout(groupedColors: Map<Color, List<Color>>, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        groupedColors.forEach { (_, colorList) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                colorList.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}

