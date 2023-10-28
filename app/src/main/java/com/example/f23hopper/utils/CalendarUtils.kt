package com.example.f23hopper.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.f23hopper.data.DayValidationError
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.getShiftColor
import com.example.f23hopper.ui.calendar.isWeekday
import com.example.f23hopper.ui.calendar.maxShifts
import com.example.f23hopper.ui.icons.rememberPartlyCloudyNight
import com.example.f23hopper.ui.icons.rememberSunny
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.LocalDate
import java.lang.ref.WeakReference
import java.sql.Date
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

fun Modifier.clickable(
    enabled: Boolean = true,
    showRipple: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (showRipple) LocalIndication.current else null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

@Composable
fun ShiftIcon(shiftType: ShiftType) {
    Icon(
        imageVector = if (shiftType == ShiftType.NIGHT) rememberPartlyCloudyNight() else rememberSunny(),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}


data class DayValidationResult(val isValid: Boolean, val error: DayValidationError? = null)

fun dateValidation(
    shifts: Map<ShiftType, List<Shift>>,
    date: java.time.LocalDate,
    isSpecialDay: Boolean
): DayValidationResult {
    if (shifts.isEmpty()) {
        return DayValidationResult(isValid = false, error = DayValidationError.NO_SHIFTS)
    }

    // If the date is a weekday, ensure both DAY and NIGHT shifts are present
    if (date.isWeekday()) {
        if (shifts[ShiftType.DAY] == null) {
            return DayValidationResult(
                isValid = false,
                error = DayValidationError.MISSING_DAY_SHIFT
            )
        }
        if (shifts[ShiftType.NIGHT] == null) {
            return DayValidationResult(
                isValid = false,
                error = DayValidationError.MISSING_NIGHT_SHIFT
            )
        }
    }

    // Check if there are enough shifts per day
    if (!shifts.all { it.value.size == maxShifts(isSpecialDay) }) {
        return DayValidationResult(isValid = false, error = DayValidationError.INSUFFICIENT_SHIFTS)
    }


    // Check if there are enough shifts per day
    if (!shifts.all { it.value.size == maxShifts(isSpecialDay) }) {
        return DayValidationResult(isValid = false, error = DayValidationError.INSUFFICIENT_SHIFTS)
    }

    // Check if opener present on day shift
    shifts[ShiftType.DAY]?.let { dayShifts ->
        if (dayShifts.none { it.employee.canOpen }) {
            return DayValidationResult(isValid = false, error = DayValidationError.NO_DAY_OPENER)
        }
    }

    // Check if closer present on night shift
    shifts[ShiftType.NIGHT]?.let { nightShifts ->
        if (nightShifts.none { it.employee.canClose }) {
            return DayValidationResult(isValid = false, error = DayValidationError.NO_NIGHT_CLOSER)
        }
    }

    // Check for FULL shifts
    shifts[ShiftType.FULL]?.let { fullShifts ->
        val canOpenEmployee = fullShifts.find { it.employee.canOpen }
        val canCloseEmployee = fullShifts.find { it.employee.canClose }

        val hasBoth = fullShifts.any { it.employee.canOpen && it.employee.canClose }

        if (!hasBoth && (canOpenEmployee == null || canCloseEmployee == null || canOpenEmployee.employee.employeeId == canCloseEmployee.employee.employeeId)) {
            return DayValidationResult(
                isValid = false,
                error = DayValidationError.NO_FULL_SHIFT_OPENER_CLOSER
            )
        }
    }

    return DayValidationResult(isValid = true)
}

@Composable
fun ShiftCircles(maxShifts: Int, shiftCount: Int, shiftType: ShiftType) {
    Column(
        modifier = Modifier
//            .weight(0.1f)
            .padding(start = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (index in 0 until maxShifts) {
            Box(
                modifier = if (index < shiftCount) {
                    Modifier
                        .size(8.dp)
                        .background(getShiftColor(shiftType), CircleShape)
                } else {
                    Modifier
                        .size(8.dp)
                        .border(
                            1.dp, MaterialTheme.colorScheme.onTertiaryContainer, CircleShape
                        )
                }
            )
        }
    }
}

fun java.time.LocalDate.toSqlDate(): Date =
    Date.valueOf(this.toString())

fun Date.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

fun java.util.Date.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

fun java.time.LocalDate.toKotlinxLocalDate(): LocalDate =
    LocalDate.parse(this.toString())

@Composable
fun StatusBarColorUpdateEffect(color: Color) {
    if (LocalInspectionMode.current) return // findActivity() will not work in preview.
    val activity = LocalContext.current.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current
    val observer = remember {
        StatusBarColorLifecycleObserver(activity, color.toArgb())
    }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}

@Composable
fun NavigationIcon(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(shape = CircleShape)
            .clickable(role = Role.Button, onClick = onBackClick),
    ) {
        Icon(
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
        )
    }
}

/**
 * Alternative way to find the first fully visible month in the layout.
 *
 * @see [rememberFirstVisibleMonthAfterScroll]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    // Only take non-null values as null will be produced when the
    // list is mid-scroll as no index will be completely visible.
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

/**
 * Returns the first visible month in a paged calendar **after** scrolling stops.
 *
 * @see [rememberFirstCompletelyVisibleMonth]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}

/**
 * Find first visible week in a paged week calendar **after** scrolling stops.
 */
@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

/**
 * Find the first month on the calendar visible up to the given [viewportPercent] size.
 *
 * @see [rememberFirstCompletelyVisibleMonth]
 * @see [rememberFirstVisibleMonthAfterScroll]
 */
@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }

        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }

        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

internal fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

class StatusBarColorLifecycleObserver(
    activity: Activity,
    @ColorInt private val color: Int,
) : DefaultLifecycleObserver {
    private val isLightColor = ColorUtils.calculateLuminance(color) > 0.5
    private val defaultStatusBarColor =
        "#CEE9DB".toColorInt() //activity.getColorCompat(Color.Green.value.toInt())
    private val activity = WeakReference(activity)

    override fun onStart(owner: LifecycleOwner) {
        activity.get()?.window?.apply {
            statusBarColor = color
            if (isLightColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
    /*

        override fun onStop(owner: LifecycleOwner) {
            activity.get()?.window?.apply {
                statusBarColor = defaultStatusBarColor
                if (isLightColor) decorView.systemUiVisibility = 0
            }
        }
     */

    override fun onDestroy(owner: LifecycleOwner) = activity.clear()
}
