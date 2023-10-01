package com.example.f23hopper.ui.employee

import EmployeeRepository
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
    /*mutableStateOf allows for ui to react to changes in variable*/
    var firstName by remember{ mutableStateOf("") }
    var lastName by remember{ mutableStateOf("") }
    var email by remember{ mutableStateOf("") }
    var phoneNumber by remember{ mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(title = {"Add Employee"})
        }
    ){innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(
                value = employeeDetails.firstName,
                onValueChange = {onEmployeeValueChange(employeeDetails.copy(firstName=it))},
                label = { Text("First Name") }
            )
            OutlinedTextField(
                value = employeeDetails.lastName,
                onValueChange = {onEmployeeValueChange(employeeDetails.copy(lastName=it))},
                label = { Text("Last Name") }
            )
            OutlinedTextField(
                value = employeeDetails.email,
                onValueChange = {onEmployeeValueChange(employeeDetails.copy(email=it))},
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = employeeDetails.phoneNumber,
                onValueChange = {onEmployeeValueChange(employeeDetails.copy(phoneNumber=it))},
                label = { Text("Phone Number") }
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

@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    EmployeeEntryScreen()
}