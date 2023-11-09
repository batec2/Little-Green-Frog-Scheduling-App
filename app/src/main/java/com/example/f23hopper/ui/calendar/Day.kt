package com.example.f23hopper.ui.calendar

import InvalidDayIcon
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition

data class DayContext(
    val viewModel: CalendarViewModel,
    val day: CalendarDay,
    val shiftsOnDay: Map<ShiftType, List<Shift>>,
    val isSpecialDay: Boolean,
    val isSelected: Boolean,
    val employeeShiftSelected: Boolean,//For employee highlighting
    val viewItemList: List<ViewItem>
)

@Composable
fun Day(
    context: DayContext,
    onClick: (CalendarDay) -> Unit = {},
) {
    val dayBackgroundColor = when {
        context.isSpecialDay -> specialDayColor
        context.day.position != DayPosition.MonthDate -> inActiveDayBackgroundColor
        else -> itemBackgroundColor
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // TODO is this to be used? @Crush
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.secondaryContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box( // Square days!!
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = if (context.isSelected) 1.dp else 0.dp,
                color = if (context.isSelected) selectedItemColor else Color.Transparent
            )
            .padding(1.dp)
            .background(/*if (context.employeeShiftSelected) color else*/ dayBackgroundColor)
            .clickable(
                enabled = true,/*day.position == DayPosition.MonthDate,*/
                onClick = { onClick(context.day) })
    ) {
        val textColor = when (context.day.position) {
            DayPosition.MonthDate -> dayTextColor
            DayPosition.InDate, DayPosition.OutDate -> inActiveDayTextColor // Grey out days not in current month
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
        ShiftViewIndicators(
            context = context,
            modifier = Modifier.align(Alignment.Center)
        )
        ColorGroupLayout(
            groupedColors = generateGroupedColors(context.shiftsOnDay),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ShiftViewIndicators(
    context: DayContext,
    modifier: Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(15.dp)
            .padding(1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val ids =
            context.shiftsOnDay.flatMap { (_, v) -> v.map { (it.employee) } }.toSet()

        context.viewItemList.forEachIndexed { _, item ->
            if (context.employeeShiftSelected && ids.contains(item.empItem)) {
                val alphaAnimation = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }
                LaunchedEffect(Unit) {
                    alphaAnimation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 1000 // 1 seconds
                        )
                    )
                }
                Column(
                    Modifier
                        .graphicsLayer { alpha = alphaAnimation.value }
                        .size(6.dp)
                        .background(item.getColor(), CircleShape)
                ) {}
            }
        }
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
                    .padding(vertical = 2.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                colorList.forEachIndexed { index, color ->
                    if (index < colorList.size) {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    val alphaAnimation = remember {
                        androidx.compose.animation.core.Animatable(0f)
                    }
                    LaunchedEffect(Unit) {
                        alphaAnimation.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 1000 // 1 seconds
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .graphicsLayer { alpha = alphaAnimation.value }
                            .size(5.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}
