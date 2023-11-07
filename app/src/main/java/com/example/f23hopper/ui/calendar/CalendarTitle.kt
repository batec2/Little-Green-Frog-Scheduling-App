package com.example.f23hopper.ui.calendar

import AbsentEmployeeIcon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.utils.displayText
import java.time.YearMonth

@Composable
fun CalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    shifts: List<Shift> = emptyList(),
    employees: List<Employee> = emptyList(),
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
    onExportClick: () -> Unit,
    onGenerateClick: () -> Unit
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = rememberVectorPainter(image = Icons.Outlined.KeyboardArrowLeft),
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            AbsentEmployeeIcon(
                shifts = shifts,
                month = currentMonth,
                allEmployees = employees,
                onGenerationClick = onGenerateClick
            )
            Text(
                text = currentMonth.displayText(),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = onExportClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = "Export to CSV"
                )
            }
        }
        CalendarNavigationIcon(
            icon = rememberVectorPainter(image = Icons.Outlined.KeyboardArrowRight),
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    tintColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Icon(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        painter = icon,
        contentDescription = contentDescription,
        tint = tintColor
    )
}
