package com.example.f23hopper.ui.employee

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.icons.rememberFilterList
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.rememberLockOpen
import com.example.f23hopper.ui.icons.rememberPartlyCloudyNight
import com.example.f23hopper.ui.icons.rememberRedo
import com.example.f23hopper.ui.icons.rememberWbSunny
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit,
    navigateToEmployeeEdit: () -> Unit,
    sharedViewModel: EmployeeListViewModel
) {
    val colorScheme = MaterialTheme.colorScheme
    val employees by sharedViewModel.employees.asFlow().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    StatusBarColorUpdateEffect(toolbarColor)//top status bar colour
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.secondaryContainer,
                    titleContentColor = colorScheme.secondaryContainer,
                    navigationIconContentColor = colorScheme.primary,
                    actionIconContentColor = colorScheme.primary
                ),
                navigationIcon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "add",
                        modifier = Modifier
                            .clickable { navigateToEmployeeAdd() }
                            .size(40.dp)
                    )
                },
                title = {},
                actions = {
                    var isExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = rememberFilterList(),
                            contentDescription = "Filter",
                            modifier = Modifier.clickable { isExpanded = true },
                        )
                    }
                    FilterDialogue(
                        isFilterExpanded = isExpanded,
                        filterState = { isExpanded = it },
                    ) { filter -> sharedViewModel.filterEmployee(filter) }
                },

                modifier = Modifier.height(50.dp),
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                EmployeeListItem(
                    employees,
                    deactivateItem =
                    {
                        coroutineScope.launch {
                            sharedViewModel.deactivateEmployee(it)
                        }
                    },
                ) { navigateToEdit ->
                    println("this: ${navigateToEdit.firstName}")
                    sharedViewModel.setEmployee(navigateToEdit)
                    navigateToEmployeeEdit()
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListItem(
    employees: List<Employee>,
    deactivateItem: (Employee) -> Unit,
    onEmployeeClick: (Employee) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var confirmDelete by remember {
        mutableStateOf(false);
    }
    LazyColumn(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = employees, key = { employee -> employee.employeeId }) { employee ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    true
                },
                //positionalThreshold =
            )
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            DismissValue.Default -> colorScheme.onPrimary
                            DismissValue.DismissedToStart -> colorScheme.tertiary
                            DismissValue.DismissedToEnd -> colorScheme.tertiary
                        }, label = ""
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(16.dp),
                        Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                modifier = Modifier.weight(.25f)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .clickable
                                        {
                                            coroutineScope.launch {
                                                dismissState.reset()
                                            }
                                        },
                                    imageVector = rememberRedo(), // mirrored undo
                                    contentDescription = "Undo"
                                )
                            }
                            Column(
                                modifier = Modifier.weight(.25f)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .weight(.25f)
                                        .clickable
                                        {
                                            deactivateItem(employee)
                                            coroutineScope.launch {
                                                dismissState.reset()
                                            }
                                        },
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            }

                        }
                    }
                },
                dismissContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(2.dp))
                            .clickable {
                                onEmployeeClick(employee)
                            }
                            .border(
                                2.dp,
                                shape = RoundedCornerShape(2.dp),
                                color = colorScheme.secondaryContainer
                            )
                            .background(colorScheme.background)
                            .padding(16.dp)
                    ) {
                        Column {
                            ListEmployeeInfo(employee = employee)
                            ListScheduleInfo(employee = employee)
                        }
                    }
                },
            )
        }
    }
}

//Need to names and nickname position
@Composable
fun ListEmployeeInfo(
    employee: Employee
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = if (employee.nickname.isNotBlank()) ("${employee.firstName} \"${employee.nickname}\" ${employee.lastName}")
                else (employee.firstName + " " + employee.lastName),
                style = TextStyle(fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

        }
        Row(
            modifier = Modifier.weight(1f)
            //horizontalArrangement = Arrangement.Start
        ) {
            if (employee.canOpen) {
                Icon(
                    imageVector = rememberLockOpen(),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Can Open"
                )
            }
            if (employee.canClose) {
                Icon(
                    imageVector = rememberLock(),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Can Close"
                )
            }

        }

    }
}


@Composable
fun ListScheduleInfo(
    employee: Employee
) {
    Row {
        val week = listOf(
            Pair(employee.sunday, "S"),
            Pair(employee.monday, "M"),
            Pair(employee.tuesday, "T"),
            Pair(employee.wednesday, "W"),
            Pair(employee.thursday, "R"),
            Pair(employee.friday, "F"),
            Pair(employee.saturday, "S")
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, shape = RoundedCornerShape(2.dp), color = colorScheme.secondary)
                .background(color = colorScheme.secondaryContainer),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = rememberWbSunny(),
                modifier = Modifier.size(20.dp),
                contentDescription = "Day Shift"
            )
            week.forEach { week ->
                Text(
                    text = if (week.first == ShiftType.DAY ||
                        week.first == ShiftType.FULL
                    ) week.second else "",
                    color = colorScheme.onSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.size(5.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, shape = RoundedCornerShape(2.dp), color = colorScheme.secondary)
                .background(color = colorScheme.secondaryContainer),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = rememberPartlyCloudyNight(),
                modifier = Modifier.size(20.dp),
                contentDescription = "Night Shift"
            )
            week.forEach { week ->
                Text(
                    text = if (week.first == ShiftType.NIGHT ||
                        week.first == ShiftType.FULL
                    ) week.second else " ",
                    color = colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun FilterDialogue(
    isFilterExpanded: Boolean,
    filterState: (Boolean) -> Unit,
    filterSelection: (String) -> Unit,
) {
    var currentSelected by remember { mutableStateOf("All Employees") }
    DropdownMenu(expanded = isFilterExpanded, onDismissRequest = { filterState(false) }
    ) {
        var selections =
            listOf(
                "All Employees",
                "Can Open",
                "Can Close",
                "Can Work Weekend",
                "Inactive"
            )

        selections.forEach { item ->
            DropdownMenuItem(
                text = { Text(item) },
                onClick = {
                    filterSelection(item)
                    filterState(false)
                    currentSelected = item
                },
                trailingIcon = {
                    val isSelected = currentSelected == item
                    if (isSelected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "check")
                    }
                }

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {

}
