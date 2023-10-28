package com.example.f23hopper.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.CustomColor
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.data.specialDay.SpecialDay
import com.example.f23hopper.ui.icons.rememberError
import com.example.f23hopper.utils.ShiftCircles
import com.example.f23hopper.utils.ShiftIcon
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import com.example.f23hopper.utils.dateValidation
import com.example.f23hopper.utils.displayText
import com.example.f23hopper.utils.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId


@Composable
fun CalendarScreen(navigateToShiftView: (String) -> Unit) {
//    val coroutineScope = rememberCoroutineScope()
    var selection by rememberSaveable { mutableStateOf<CalendarDay?>(null) }

    val viewModel = hiltViewModel<CalendarViewModel>()
    val shifts by viewModel.parsedShifts.collectAsState(initial = emptyList())
    val specialDays by viewModel.parsedDays.collectAsState(initial = emptyList())

    Calendar(shifts, specialDays, navigateToShiftView, viewModel, selection) {
        selection = it
    }
}

@Composable
fun Calendar(
    shifts: List<Shift>,
    specialDays: List<SpecialDay>,
    navigateToShiftView: (String) -> Unit,
    viewModel: CalendarViewModel,
    selection: CalendarDay?,
    onSelectionChanged: (CalendarDay?) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }

//    var selection = selection
    val shiftsOnSelectedDate = if (selection?.date != null) {
        shifts.filter { it.schedule.date.toString() == selection.date.toString() }
            .groupBy { ShiftType.values()[it.schedule.shiftTypeId] }
    } else emptyMap()

    val shiftsByDay =
        shifts.groupBy { it.schedule.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }

    val specialDaysByDay =
        specialDays.groupBy { it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }
    StatusBarColorUpdateEffect(toolbarColor)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundColor),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek().first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

//        LaunchedEffect(visibleMonth) {
//            selection = null
//        }

        SimpleCalendarTitle(
            modifier = Modifier
                .background(toolbarColor)
                .padding(horizontal = 8.dp, vertical = 0.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )
        HorizontalCalendar(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            dayContent = { day ->
                val isSpecialDay = specialDaysByDay[day.date] != null
                val context = DayContext(
                    viewModel = viewModel, day = day,
                    shiftsOnDay = shiftsByDay[day.date]?.groupBy { ShiftType.values()[it.schedule.shiftTypeId] }
                        .orEmpty(),
                    isSpecialDay = isSpecialDay,
                    isSelected = selection == day
                )
                Day(
                    context,
                ) { clicked ->
                    onSelectionChanged(clicked)
//                    navigateToDayView(clicked.date.toString())
                }
            },
            monthHeader = {
                WeekDays(modifier = Modifier.padding(vertical = 8.dp))
            },
        )
//        Divider(color = itemBackgroundColor)
        if (selection != null) {  // Conditionally render based on selection
            Divider(color = itemBackgroundColor)
            val isSpecialDay = specialDaysByDay[selection?.date!!] != null
            ShiftDetailsForDay(
                shiftsOnSelectedDate,
                selection?.date!!,
                isSpecialDay = isSpecialDay,
                navigateToShiftView
            )

        }
    }
}


data class DayContext(
    val viewModel: CalendarViewModel,
    val day: CalendarDay,
    val shiftsOnDay: Map<ShiftType, List<Shift>>,
    val isSpecialDay: Boolean,
    val isSelected: Boolean,
)

@Composable
private fun Day(
    context: DayContext,
    onClick: (CalendarDay) -> Unit = {},
) {
    Box( // Square days!!
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = if (context.isSelected) 1.dp else 0.dp,
                color = if (context.isSelected) selectedItemColor else Color.Transparent
            )
            .padding(1.dp)
            .background(if (context.isSpecialDay) CustomColor.specialDay else itemBackgroundColor)
            .clickable(
                enabled = true,/*day.position == DayPosition.MonthDate,*/
                onClick = { onClick(context.day) })
    ) {
        val textColor = when (context.day.position) {
            DayPosition.MonthDate -> MaterialTheme.colorScheme.onBackground
            DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor // Grey out days not in current month
        }


        InvalidDayIcon(context, Modifier.align(Alignment.TopEnd))

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
fun InvalidDayIcon(context: DayContext, modifier: Modifier) {
    val dayValidation = dateValidation(context.shiftsOnDay, context.day.date, context.isSpecialDay)
    if (!dayValidation.isValid) {
        Icon(
            modifier = modifier
                .padding(2.dp)
                .size(15.dp, 15.dp),
            imageVector = rememberError(),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = dayValidation.error.toString()
        )
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

@Composable
private fun WeekDays(modifier: Modifier) {
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
fun ShiftDetailsForDay(
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


@Composable
fun getColorDateMap(eventsByDay: Map<LocalDate, List<Shift>>): Map<LocalDate, List<Color>> {
    return eventsByDay.mapValues { entry ->
        entry.value.map { event ->
            val shiftType = ShiftType.values()[event.schedule.shiftTypeId]
            getShiftColor(shiftType)
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

val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val selectedItemColor: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val inActiveTextColor: Color @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

fun LocalDate.isWeekday() =
    !(this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY)

fun maxShifts(isSpecialDay: Boolean) = if (isSpecialDay) 3 else 2
fun maxShiftRows(date: LocalDate) = if (date.isWeekday()) 2 else 1
