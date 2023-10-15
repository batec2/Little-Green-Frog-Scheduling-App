package com.example.f23hopper.ui.employee

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@Composable
fun EmployeeEditScreen(
    navigateToEmployeeList:() -> Unit,
    sharedViewModel: EmployeeListViewModel
){
    StatusBarColorUpdateEffect(toolbarColor)//top status bar colour
    val coroutineScope = rememberCoroutineScope()
    val viewModel = sharedViewModel
    //val employeeDetails = viewModel.employeeUiState.employeeDetails
    //Text(text = "This = ${viewModel.employeeUiState.employeeDetails.firstName}")
    EmployeeEntryBody(
        employeeUiState = viewModel.employeeUiState,
        employeeDetails = viewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveEmployee()
            }
        },
        navigateToEmployeeList = navigateToEmployeeList
    )

}

