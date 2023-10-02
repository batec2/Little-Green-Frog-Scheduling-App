package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorScheme.tertiaryContainer,
                contentColor = colorScheme.onTertiaryContainer,
                onClick = { navigateToEmployeeAdd() }  // Navigates to employee

            ) {
                Row {
                    /*PLACE FILTERS AND BUTTONS HERE*/
                }
                Column {
                    /*PLACE LAZY LIST HERE FOR EMPLOYEES*/
                }
                Icon(
                    Icons.Default.Add,
                    contentDescription = "add",
                    tint = colorScheme.onPrimary
                )  // Explicitly set icon tint from theme
            }
        },
        content = { innerPadding ->
            // Your content here
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {

}