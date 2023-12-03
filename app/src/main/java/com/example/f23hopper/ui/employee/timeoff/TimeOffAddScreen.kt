package com.example.f23hopper.ui.employee.timeoff

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.example.f23hopper.utils.CalendarUtilities.toKotlinxLocalDate
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
        onSaveClick = { /*TODO*/ },
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
    var startDate = state.selectedStartDateMillis
    var endDate = state.selectedEndDateMillis

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
                    modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                        onSaveClick()
                    }, //enabled = employeeUiState.isEmployeeValid
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
                EmpPickerState = {showEmpPicker.value=it}
            )
            Spacer(modifier=Modifier.size(10.dp))
            OutlinedTextField(
                value =
                if(startDate==null)
                    "Start Date"
                else
                    convertMillisToDate(startDate).toString(),
                onValueChange = {},
                placeholder = { Text(text = "Start Date") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Start")
                },
            )
            Spacer(modifier=Modifier.size(10.dp))
            OutlinedTextField(
                value =
                if(endDate==null)
                    "End Date"
                else
                    convertMillisToDate(endDate).toString(),
                onValueChange = {},
                placeholder = { Text(text = "End Date") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Start")
                },
            )
            ElevatedButton(onClick = {showTimeOffPicker.value=true}) {
                Text(text="Choose Date")
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffPicker(
    showTimeOffPicker: ()->Unit,
    state: DateRangePickerState
){
    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            ElevatedButton(
                modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                    showTimeOffPicker()
                },
            ) {
                Text(text = "Done")
            }
        }) {
        Column {
            DateRangePicker(
                modifier = Modifier,
                state = state,
            )
            Row (
                modifier = Modifier,
            ){
                /*

                ElevatedButton(
                    modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                        showTimeOffPicker()
                    },
                ) {
                    Text(text = "Cancel")
                }
                 */
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeePicker(
    employees: List<Employee>,
    showEmpPicker: Boolean,
    EmpPickerState:(Boolean)->Unit,
){
    val selection = remember{ mutableStateOf("Employee") }
    ExposedDropdownMenuBox(
        expanded = showEmpPicker,
        onExpandedChange = {EmpPickerState(it)}
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
            onDismissRequest = { EmpPickerState(false) }
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
                        EmpPickerState(false)
                    }
                )
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): LocalDate {
    return Date(millis).toJavaLocalDate()
}