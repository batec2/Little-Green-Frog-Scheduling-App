package com.example.f23hopper.ui.employee.timeoff

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.utils.CalendarUtilities.toJavaLocalDate
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import com.example.f23hopper.utils.clickable
import java.sql.Date
import java.time.LocalDate

@Composable
fun TimeOffAddScreen(
    navigateToTimeOff: () -> Unit,
    sharedViewModel: TimeOffViewModel,
){
    StatusBarColorUpdateEffect(toolbarColor)
    val coroutineScope = rememberCoroutineScope()
    TimeOffBody(
        sharedViewModel = sharedViewModel,
        onSaveClick = {  },
        navigateToEmployeeTimeOff = navigateToTimeOff)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffBody(
    sharedViewModel: TimeOffViewModel,
    onSaveClick: () -> Unit,
    navigateToEmployeeTimeOff: () -> Unit,
){
    val showTimeOffPicker = remember { mutableStateOf(false) }
    val showEmpPicker = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val employees by sharedViewModel.employeesList.asFlow().collectAsState(initial = emptyList())
    val state = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
        yearRange = ((calendar[Calendar.YEAR]..(calendar[Calendar.YEAR]+1)))
    )
    val start = state.selectedStartDateMillis
    val end = state.selectedStartDateMillis

    sharedViewModel.timeOffUiState.start = start
    sharedViewModel.timeOffUiState.end = end

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {},
            navigationIcon = {
                IconButton(onClick = { navigateToEmployeeTimeOff() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back To list"
                    )
                }
            },
            actions = {
                ElevatedButton(
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        sharedViewModel.addTimeOff()
                    }, enabled = sharedViewModel.timeOffUiState.isTimeOffValid
                ) {
                    Text(text = "Done")
                }
            },
            modifier = Modifier.height(50.dp),
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier=Modifier.size(10.dp))
            EmployeePicker(
                employees = employees,
                showEmpPicker = showEmpPicker.value,
                empPickerState = {showEmpPicker.value=it},
                onEmployeeSelect = { sharedViewModel.timeOffUiState.employee = it }
            )
            Spacer(modifier=Modifier.size(10.dp))
            DateBox(
                date = state.selectedStartDateMillis,
                placeholder = "Start Date",
                onDateClick = { showTimeOffPicker.value = true }
            )
            Spacer(modifier=Modifier.size(10.dp))
            DateBox(
                date = state.selectedEndDateMillis,
                placeholder = "End Date",
                onDateClick = { showTimeOffPicker.value = true }
            )
            if(showTimeOffPicker.value){
                TimeOffPicker(
                    showTimeOffPicker = {
                        showTimeOffPicker.value = false
                    },
                    state = state,
                )
            }
        }
    }
}

@Composable
fun DateBox(
    date: Long?,
    placeholder: String,
    onDateClick: ()->Unit
){
    OutlinedTextField(
        value =
        if(date==null)
            placeholder
        else
            convertToDate(date).toString(),
        onValueChange = {},
        placeholder = { Text(text = placeholder) },
        readOnly = true,
        trailingIcon = {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = placeholder)
        },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = Modifier.clickable { onDateClick() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffPicker(
    showTimeOffPicker: ()->Unit,
    state: DateRangePickerState
){
    DatePickerDialog(
        onDismissRequest = {showTimeOffPicker()},
        confirmButton = {}
    ) {
        DateRangePicker(
            modifier = Modifier,
            state = state,
        )
        Row (
            modifier = Modifier,
        ){
            ElevatedButton(
                modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                    showTimeOffPicker()
                },
            ) {
                Text(text = "Done")
            }
            ElevatedButton(
                modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                    showTimeOffPicker()
                },
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

/**
 * Dropdown menu for selecting a Employee for timeoff selection
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeePicker(
    employees: List<Employee>,
    showEmpPicker: Boolean,
    empPickerState:(Boolean)->Unit,
    onEmployeeSelect: (Employee)->Unit
){
    val selection = remember{ mutableStateOf("Employee") }
    ExposedDropdownMenuBox(
        expanded = showEmpPicker,
        onExpandedChange = {empPickerState(it)}
    ) {
        OutlinedTextField(
            value = selection.value,
            placeholder = { Text(text = "Employee")},
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showEmpPicker)
            },
            modifier = Modifier
                .menuAnchor(),
        )
        ExposedDropdownMenu(
            expanded = showEmpPicker,
            onDismissRequest = { empPickerState(false) }
        ) {
            employees.forEach{employee ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = employee.firstName +" "+employee.lastName,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        selection.value = employee.firstName +" "+employee.lastName
                        onEmployeeSelect(employee)
                        empPickerState(false)
                    }
                )
            }
        }
    }
}

/**
 * Converts Millis(Long) to JavaLocalDate
 */
fun convertToDate(millis: Long): LocalDate {
    return Date(millis).toJavaLocalDate()
}