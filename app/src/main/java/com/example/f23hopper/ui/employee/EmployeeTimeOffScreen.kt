package com.example.f23hopper.ui.employee

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.asFlow
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.utils.StatusBarColorUpdateEffect

@Composable
fun EmployeeTimeOffScreen(
    navigateToEmployeeList: () -> Unit,
    viewModel: EmployeeTimeOffViewModel
) {
    val colorScheme = MaterialTheme.colorScheme
    val employees by viewModel.timeOffList.asFlow().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    StatusBarColorUpdateEffect(toolbarColor) // Top status bar color



}