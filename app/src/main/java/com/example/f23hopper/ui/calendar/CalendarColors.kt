package com.example.f23hopper.ui.calendar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val selectedItemColor: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val inActiveTextColor: Color @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

