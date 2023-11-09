package com.example.f23hopper.ui.employee

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.components.BaseDialog
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
        viewModel = sharedViewModel,
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
                    if (validateInput(
                            sharedViewModel.employeeUiState.employeeDetails,
                            sharedViewModel.employees.value ?: emptyList()
                        )
                    ) {
                        sharedViewModel.saveEmployee()
                    }
                }
            }
        },
        navigateToEmployeeList = navigateToEmployeeList,
        showConfirmationDialog = showConfirmationDialog
    )

    // Confirmation dialog
    if (showConfirmationDialog.value) {
        CriticalShiftDialog(
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
fun CriticalShiftDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = "Active Open/Close Shifts",
        message = "Employee is closer/opener on active shifts, are you sure you want to remove their certification?",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        headerSize = MaterialTheme.typography.headlineSmall
    )
}
