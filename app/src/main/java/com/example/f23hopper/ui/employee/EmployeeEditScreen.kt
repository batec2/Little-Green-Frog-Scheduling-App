package com.example.f23hopper.ui.employee

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.launch

@Composable
fun EmployeeEditScreen(
    navigateToEmployeeList: () -> Unit,
    sharedViewModel: EmployeeListViewModel,
    entryViewModel: EmployeeEntryViewModel = hiltViewModel()
) {

    StatusBarColorUpdateEffect(toolbarColor)
    val coroutineScope = rememberCoroutineScope()

    // State to control the visibility of the confirmation dialog
    val showConfirmationDialog = remember { mutableStateOf(false) }

    EmployeeEntryBody(
        employeeUiState = sharedViewModel.employeeUiState,
        employeeDetails = sharedViewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = sharedViewModel::updateUiState,
        onSaveClick = {
            if (entryViewModel.employeeOnlyOpenerCloserCheck(sharedViewModel.employeeUiState)) {
                // Show confirmation dialog if the employee is critical
                showConfirmationDialog.value = true
            } else {
                // Save employee if not critical
                coroutineScope.launch {
                    sharedViewModel.saveEmployee()
                    navigateToEmployeeList()
                }
            }
        },
        navigateToEmployeeList = navigateToEmployeeList,
        showConfirmationDialog = showConfirmationDialog
    )

    // Confirmation dialog
    if (showConfirmationDialog.value) {
        ConfirmationDialog(
            onConfirm = {
                coroutineScope.launch {
                    sharedViewModel.saveEmployee()
                    navigateToEmployeeList()
                }
                showConfirmationDialog.value = false
            },
            onDismiss = {
                showConfirmationDialog.value = false
            }
        )
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("No")
            }
        },
        title = {
            Text("Confirmation")
        },
        text = {
            Text("Employee is closer/opener on active shifts, are you sure you want to remove their certification?")
        }
    )
}
