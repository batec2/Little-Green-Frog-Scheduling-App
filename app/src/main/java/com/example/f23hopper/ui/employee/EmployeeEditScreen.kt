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

enum class EmployeeErrorType {
    CERTIFICATION_ERROR,
    MAX_SHIFT_ERROR
}

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
    val activeErrors = remember { mutableStateOf<Set<EmployeeErrorType>>(setOf()) }
    EmployeeEntryBody(
        viewModel = sharedViewModel,
        employeeUiState = sharedViewModel.employeeUiState,
        employeeDetails = sharedViewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = sharedViewModel::updateUiState,
        onSaveClick = {
            var hasError = false

            if (entryViewModel.employeeOnlyOpenerCloserCheck(sharedViewModel.employeeUiState)) {
                activeErrors.value = activeErrors.value + EmployeeErrorType.CERTIFICATION_ERROR
                showConfirmationDialog.value = true
                hasError = true
            }
            if (entryViewModel.employeeScheduledForMoreThanMaxShifts(sharedViewModel.employeeUiState)) {
                activeErrors.value = activeErrors.value + EmployeeErrorType.MAX_SHIFT_ERROR
                showConfirmationDialog.value = true
                hasError = true
            }

            if (!hasError) {
                activeErrors.value = activeErrors.value - EmployeeErrorType.CERTIFICATION_ERROR
                activeErrors.value = activeErrors.value - EmployeeErrorType.MAX_SHIFT_ERROR
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
        EmployeeWarning(
            activeErrors = activeErrors.value,
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
fun EmployeeWarning(
    activeErrors: Set<EmployeeErrorType>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val message = buildString {
        if (EmployeeErrorType.CERTIFICATION_ERROR in activeErrors) {
            appendLine("- Employee is sole closer/opener on active shifts.\n")
        }
        if (EmployeeErrorType.MAX_SHIFT_ERROR in activeErrors) {
            appendLine("- Employee is scheduled for more shifts in a future week than their maximum shift limit allows.\n")

        }
        appendLine("\nPlease review and adjust the schedule as needed.\n\nConfirm Changes?")
    }

    BaseDialog(
        title = "Warning",
        message = message,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        headerSize = MaterialTheme.typography.headlineSmall
    )
}
