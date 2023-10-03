package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryScreen() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<EmployeeEntryViewModel>()
    EmployeeEntryBody(
        employeeUiState = viewModel.employeeUiState,
        employeeDetails = viewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveEmployee()
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryBody(
    employeeUiState: EmployeeUiState,
    employeeDetails: EmployeeDetails,
    onEmployeeValueChange:(EmployeeDetails) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmployeeInfo(
                onEmployeeInfoChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            WeekendSelector(
                onWeekendValueChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            Button(
                onClick = onSaveClick,
                enabled = employeeUiState.isEmployeeValid,
            ) {
                Text(text = "Add")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmployeeInfo(
    onEmployeeInfoChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
){

    val focusManager = LocalFocusManager.current

    val handleKeyEvent: (KeyEvent) -> Boolean = {
        when {
            (it.type == KeyEventType.KeyDown && (it.key == Key.Tab || it.key == Key.Enter)) -> {
                focusManager.moveFocus(FocusDirection.Next)
                true
            }
            else -> false
        }
    }

    //TODO: Refactor out this repetition. Structs for the field, for loop of structs into a component?
    OutlinedTextField(
        modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
        value = employeeDetails.firstName,
        onValueChange = { onEmployeeInfoChange(employeeDetails.copy(firstName = it)) },
        label = { Text("First Name") }
    )
    OutlinedTextField(
        modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
        value = employeeDetails.lastName,
        onValueChange = { onEmployeeInfoChange(employeeDetails.copy(lastName = it)) },
        label = { Text("Last Name") }
    )
    OutlinedTextField(
        modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
        value = employeeDetails.email,
        onValueChange = { onEmployeeInfoChange(employeeDetails.copy(email = it)) },
        label = { Text("Email") }
    )
    OutlinedTextField(
        modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
        value = employeeDetails.phoneNumber,
        onValueChange = { onEmployeeInfoChange(employeeDetails.copy(phoneNumber = it)) },
        label = { Text("Phone Number") }
    )
}

@Composable
fun WeekendSelector(
    onWeekendValueChange:(EmployeeDetails) -> Unit ={},
    employeeDetails:EmployeeDetails
){
    var checkedOpen by remember{mutableStateOf(false)}
    var checkedClose by remember{mutableStateOf(false)}
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Opening")
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedOpen,
            onCheckedChange = {
                checkedOpen = it
                onWeekendValueChange(employeeDetails.copy(canOpen = it))
            },
        )
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Text(text = "Closing")
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedClose,
            onCheckedChange = {
                checkedClose = it
                onWeekendValueChange(employeeDetails.copy(canClose = it))
            },
        )

    }
}

@Composable
fun ScheduleSelector(
    day: String,
    employeeDetails: EmployeeDetails,
    onSelectionChange: (EmployeeDetails) -> Unit = {},

    ) {
    var sliderPosition by remember { mutableStateOf(0f) }
    var text by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier,
            //.width(50.dp)
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(text = day)
            Slider(
                value = sliderPosition,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                ),
                steps = 1,
                valueRange = 0f..2f
            )
            text = when (sliderPosition) {
                0f -> ShiftType.DAY.toString()
                1f -> ShiftType.FULL.toString()
                else -> ShiftType.NIGHT.toString()
            }
        }
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    //ScheduleSelector(day="monday")
}