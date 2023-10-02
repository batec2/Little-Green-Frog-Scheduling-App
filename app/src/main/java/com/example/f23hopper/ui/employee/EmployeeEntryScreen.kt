package com.example.f23hopper.ui.employee

import EmployeeRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.f23hopper.data.EmployeeDao
import com.example.f23hopper.data.EmployeesDatabase
import com.example.f23hopper.ui.AppViewModelProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.f23hopper.data.ShiftType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryScreen(
    viewModel: EmployeeEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    EmployeeEntryBody(
        employeeUiState = viewModel.employeeUiState,
        employeeDetails = viewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch{
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
){
    Scaffold(
        topBar = {
            TopAppBar(title = {})
        }
    ){innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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

@Composable
fun EmployeeInfo(
    onEmployeeInfoChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
){
    OutlinedTextField(
        value = employeeDetails.firstName,
        onValueChange = {onEmployeeInfoChange(employeeDetails.copy(firstName=it))},
        label = { Text("First Name") }
    )
    OutlinedTextField(
        value = employeeDetails.lastName,
        onValueChange = {onEmployeeInfoChange(employeeDetails.copy(lastName=it))},
        label = { Text("Last Name") }
    )
    OutlinedTextField(
        value = employeeDetails.email,
        onValueChange = {onEmployeeInfoChange(employeeDetails.copy(email=it))},
        label = { Text("Email") }
    )
    OutlinedTextField(
        value = employeeDetails.phoneNumber,
        onValueChange = {onEmployeeInfoChange(employeeDetails.copy(phoneNumber=it))},
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
    ){
        Text(text="Opening")
        Spacer(modifier = Modifier.padding(start=5.dp))
        Switch(
            checked = checkedOpen,
            onCheckedChange = {
                checkedOpen = it
                onWeekendValueChange(employeeDetails.copy(canOpen = it))
            },
        )
        Spacer(modifier = Modifier.padding(start=5.dp))
        Text(text="Closing")
        Spacer(modifier = Modifier.padding(start=5.dp))
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
    day:String,
    employeeDetails: EmployeeDetails,
    onSelectionChange: (EmployeeDetails) -> Unit = {},

){
    var sliderPosition by remember { mutableStateOf(0f) }
    var text by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier,
                //.width(50.dp)
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            Text(text=day)
            Slider(
                value = sliderPosition,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                ),
                steps = 1,
                valueRange = 0f..2f
            )
            if(sliderPosition == 0f){
                text = ShiftType.DAY.toString()
            }
            else if(sliderPosition == 1f){
                text = ShiftType.FULL.toString()
            }
            else{
                text = ShiftType.NIGHT.toString()
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