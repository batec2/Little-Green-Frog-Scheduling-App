package com.example.f23hopper.ui.employee

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.components.BaseDialog
import com.example.f23hopper.ui.icons.dayShiftIcon
import com.example.f23hopper.ui.icons.nightShiftIcon
import com.example.f23hopper.ui.icons.rememberFilterList
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.rememberRedo
import com.example.f23hopper.ui.icons.unlockIcon
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit,
    navigateToEmployeeEdit: () -> Unit,
    sharedViewModel: EmployeeListViewModel
) {
    val colorScheme = MaterialTheme.colorScheme
    val employees by sharedViewModel.employees.asFlow().collectAsState(initial = emptyList())
    val showConfirmationDialog = sharedViewModel.showConfirmationDialog
    val employeeToToggle = sharedViewModel.employeeToToggle

    val coroutineScope = rememberCoroutineScope()

    EmployeeDeactivationDialog(
        employee = employeeToToggle,
        onConfirmDeactivation = { sharedViewModel.confirmDeactivation() },
        onDismissDialog = { sharedViewModel.dismissDialog() }
    )

    StatusBarColorUpdateEffect(toolbarColor) // Top status bar color

    EmployeeListScaffold(
        colorScheme = colorScheme,
        navigateToEmployeeAdd = navigateToEmployeeAdd,
        sharedViewModel = sharedViewModel,
        employees = employees,
        coroutineScope = coroutineScope,
        navigateToEmployeeEdit = navigateToEmployeeEdit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListTopBar(
    colorScheme: ColorScheme,
    navigateToEmployeeAdd: () -> Unit,
    sharedViewModel: EmployeeListViewModel
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
            ) { filter -> sharedViewModel.filterEmployee(filter) }
        },
        modifier = Modifier.height(50.dp),
    )
}

@Composable
fun EmployeeListContent(
    paddingValues: PaddingValues,
    employees: List<Employee>,
    coroutineScope: CoroutineScope,
    sharedViewModel: EmployeeListViewModel,
    navigateToEmployeeEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        EmployeeListItem(
            employees,
            deactivateItem = { employee ->
                coroutineScope.launch {
                    sharedViewModel.toggleEmployeeActive(employee, employee.active)
                }
            },
            onEmployeeClick = { navigateToEdit ->
                println("this: ${navigateToEdit.firstName}")
                sharedViewModel.setEmployee(navigateToEdit)
                navigateToEmployeeEdit()
            }
        )
    }
}

@Composable
fun EmployeeListScaffold(
    colorScheme: ColorScheme,
    navigateToEmployeeAdd: () -> Unit,
    sharedViewModel: EmployeeListViewModel,
    employees: List<Employee>,
    coroutineScope: CoroutineScope,
    navigateToEmployeeEdit: () -> Unit
) {
    Scaffold(
        topBar = { EmployeeListTopBar(colorScheme, navigateToEmployeeAdd, sharedViewModel) },
        // I have no idea why padding values needs to be like this, but it gets real mad at me without it.
        content = { paddingValues ->
            EmployeeListContent(
                paddingValues,
                employees,
                coroutineScope,
                sharedViewModel,
                navigateToEmployeeEdit
            )
        }
    )
}

@Composable
fun EmployeeDeactivationDialog(
    employee: Employee?,
    onConfirmDeactivation: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    if (employee != null) {
        BaseDialog(
            title = "Deactivate Employee",
            message = "The employee currently has shifts scheduled for a future date, are you sure you want to deactivate?",
            onConfirm = onConfirmDeactivation,
            onDismiss = onDismissDialog,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListItem(
    employees: List<Employee>,
    deactivateItem: (Employee) -> Unit,
    onEmployeeClick: (Employee) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = employees, key = { employee -> employee.employeeId }) { employee ->
            val dismissState = DismissState(
                initialValue = DismissValue.Default,
                confirmValueChange = {
                    true
                })
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            DismissValue.Default -> colorScheme.onTertiary
                            DismissValue.DismissedToStart -> colorScheme.onTertiary
                            DismissValue.DismissedToEnd -> colorScheme.onTertiary
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
                                        },
                                    imageVector =
                                    if (employee.active) Icons.Filled.Clear
                                    else Icons.Filled.Refresh,
                                    contentDescription = "Deactivate and Reactivate"
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


@Composable
fun EmployeeNameDisplay(
    employee: Employee,
    includeLastName: Boolean = true,
    fontSize: TextUnit = 20.sp,
    modifier: Modifier = Modifier,

    ) {
    Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
        if (employee.nickname.isNotBlank()) {
            // nickname exists, show it in italics
            Text(
                text = "${employee.nickname} ",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = fontSize),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            // no nickname, show the first name (not in italics)
            Text(
                text = "${employee.firstName} ",
                style = TextStyle(fontSize = fontSize),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        // Last Name
        if (includeLastName)
            Text(
                text = employee.lastName,
                style = TextStyle(fontSize = fontSize),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
    }
}

@Composable
fun ListEmployeeInfo(employee: Employee) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.Start
        ) {
            EmployeeNameDisplay(employee = employee)
        }
        Row(
            modifier = Modifier.weight(1f)
            //horizontalArrangement = Arrangement.Start
        ) {
            if (employee.canOpen) {
                Icon(
                    imageVector = unlockIcon(),
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
            Pair(employee.sunday, "U"),
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
                imageVector = dayShiftIcon(),
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
                imageVector = nightShiftIcon(),
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
