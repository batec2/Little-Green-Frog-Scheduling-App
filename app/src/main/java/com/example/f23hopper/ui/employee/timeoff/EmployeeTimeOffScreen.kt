package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.timeoff.TimeOff
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.employee.EmployeeListContent
import com.example.f23hopper.ui.employee.EmployeeListItem
import com.example.f23hopper.ui.employee.EmployeeListTopBar
import com.example.f23hopper.ui.employee.EmployeeListViewModel
import com.example.f23hopper.ui.employee.FilterDialogue
import com.example.f23hopper.ui.icons.rememberFilterList
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EmployeeTimeOffScreen(
    navigateToEmployeeList: () -> Unit,
) {
    val viewModel = hiltViewModel<EmployeeTimeOffViewModel>()
    val colorScheme = MaterialTheme.colorScheme
    val timeoffList by viewModel.timeOffList.asFlow().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    StatusBarColorUpdateEffect(toolbarColor) // Top status bar color
    TimeOffListScaffold(
        colorScheme = colorScheme,
        navigateToEmployeeList = navigateToEmployeeList,
        viewModel = viewModel,
        timeoffList = timeoffList,
        coroutineScope = coroutineScope,
    )


}

@Composable
fun TimeOffListScaffold(
    colorScheme: ColorScheme,
    navigateToEmployeeList: () -> Unit,
    viewModel: EmployeeTimeOffViewModel,
    timeoffList: List<TimeOff>,
    coroutineScope: CoroutineScope,
) {
    Scaffold(
        topBar = { TimeOffListTopBar(colorScheme,viewModel,navigateToEmployeeList) },
        content = { paddingValues ->
            TimeOffListContent(
                paddingValues,
                timeoffList,
                coroutineScope,
                viewModel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffListTopBar(
    colorScheme: ColorScheme,
    viewModel: EmployeeTimeOffViewModel,
    navigateToEmployeeList: ()->Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.secondaryContainer,
            titleContentColor = colorScheme.secondaryContainer,
            navigationIconContentColor = colorScheme.primary,
            actionIconContentColor = colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = { navigateToEmployeeList() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = "Back To list"
                )
            }
        },
        title = { Text(text = "Employee Time Off")},
        actions = {
            Icon(
                Icons.Default.Add,
                contentDescription = "add",
                modifier = Modifier
                    .clickable { }
                    .size(40.dp)
            )
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = rememberFilterList(),
                    contentDescription = "Filter",
                    modifier = Modifier
                        .padding(top = 5.dp, end = 5.dp)
                        .size(40.dp)
                        .clickable { isExpanded = true },
                )
            }
            FilterDialogue(
                isFilterExpanded = isExpanded,
                filterState = { isExpanded = it },
            ) { }
        },
        modifier = Modifier.height(50.dp),
    )
}

@Composable
fun TimeOffListContent(
    paddingValues: PaddingValues,
    timeoffList: List<TimeOff>,
    coroutineScope: CoroutineScope,
    viewModel: EmployeeTimeOffViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
    }
}