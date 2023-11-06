package com.example.f23hopper.ui.calendar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.compose.CustomColor

val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
val inActiveDayBackgroundColor: Color @Composable get() = CustomColor.inactiveMonthDayBackground

val dayTextColor: Color @Composable get() = MaterialTheme.colorScheme.onBackground
val specialDayColor: Color @Composable get() = CustomColor.specialDay
val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val selectedItemColor: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val inActiveDayTextColor: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground.copy(
        alpha = 0.6f
    )

