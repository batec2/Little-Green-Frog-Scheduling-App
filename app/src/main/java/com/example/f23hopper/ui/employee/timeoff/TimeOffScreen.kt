package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.timeoff.TimeOff
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.employee.DismissBackground
import com.example.f23hopper.ui.employee.FilterDialogue
import com.example.f23hopper.ui.icons.rememberFilterList
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate

@Composable
fun TimeOffScreen(
    navigateToTimeOffList: () -> Unit,
    navigateToTimeOffAdd: () -> Unit,
    sharedViewModel: TimeOffViewModel,
) {
    val colorScheme = MaterialTheme.colorScheme
    val timeoffList by sharedViewModel.timeOffList.asFlow().collectAsState(initial = emptyList())
    val employeeList by sharedViewModel.employeesList.asFlow().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    StatusBarColorUpdateEffect(toolbarColor) // Top status bar color
    TimeOffListScaffold(
        colorScheme = colorScheme,
        navigateToTimeOffList = navigateToTimeOffList,
        navigateToTimeOffAdd = navigateToTimeOffAdd,
        viewModel = sharedViewModel,
        timeoffList = timeoffList,
        employeeList = employeeList,
        coroutineScope = coroutineScope,
    )


}

@Composable
fun TimeOffListScaffold(
    colorScheme: ColorScheme,
    navigateToTimeOffList: () -> Unit,
    navigateToTimeOffAdd: () -> Unit,
    viewModel: TimeOffViewModel,
    timeoffList: List<TimeOff>,
    employeeList: List<Employee>,
    coroutineScope: CoroutineScope,
) {
    Scaffold(
        topBar = { TimeOffListTopBar(colorScheme,navigateToTimeOffList,navigateToTimeOffAdd,viewModel) },
        content = { paddingValues ->
            TimeOffListContent(
                paddingValues,
                timeoffList,
                employeeList,
                coroutineScope,
                viewModel,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffListTopBar(
    colorScheme: ColorScheme,
    navigateToTimeOffList: ()->Unit,
    navigateToTimeOffAdd: () -> Unit,
    viewModel: TimeOffViewModel,
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
            IconButton(onClick = { navigateToTimeOffList() }) {
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
                    .clickable { navigateToTimeOffAdd() }
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
    employeeList: List<Employee>,
    coroutineScope: CoroutineScope,
    viewModel: TimeOffViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        EmployeeTimeOffList(
            timeoffList,
            employeeList,
            deactivateItem = {},
            onTimeOffClick = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeTimeOffList(
    timeOffList: List<TimeOff>,
    employeeList: List<Employee>,
    deactivateItem: (TimeOff) -> Unit,
    onTimeOffClick: (TimeOff) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
){
    LazyColumn(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = timeOffList, key = { timeOff -> timeOff.id }) { timeOff ->
            val employee = employeeList.filter{ e->e.employeeId==timeOff.employeeId}[0]
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToEnd) {
                        deactivateItem(timeOff)
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    DismissBackground(
                        dismissState,
                        undoAction = {
                            coroutineScope.launch {
                                dismissState.reset()
                            }
                        },
                        deactivateAction = {
                            deactivateItem(timeOff)
                            coroutineScope.launch {
                                dismissState.reset()
                            }
                        })
                },
                dismissContent = { TimeOffRow(employee,timeOff,onTimeOffClick) }
            )
        }
    }
}

@Composable
fun TimeOffRow(
    employee: Employee,
    timeOff: TimeOff,
    onTimeOffClick: (TimeOff) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(2.dp))
            .clickable { onTimeOffClick(timeOff) }
            .border(
                2.dp,
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column {
            Text(text = employee.firstName+" "+"'"+employee.nickname+"'"+employee.lastName)
            Row{
                Text(text = formatDate(timeOff.dateFrom.toJavaLocalDate()) +"-"+ formatDate(timeOff.dateTo.toJavaLocalDate()))
            }
        }
    }
}

private fun formatDate(date: LocalDate): String{
    return date.toString()
}