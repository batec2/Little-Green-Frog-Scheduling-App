package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Surface(
                shape = RoundedCornerShape(15.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Active Open/Close Shifts", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Employee is closer/opener on active shifts, are you sure you want to remove their certification?")
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("No")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onConfirm,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Yes")
                        }
                    }
                }
            }
        }
    )
}
