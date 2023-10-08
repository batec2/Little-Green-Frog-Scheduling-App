package com.example.f23hopper.ui.employee

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow

@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel = hiltViewModel<EmployeeListViewModel>()
    val employees by viewModel.employees.asFlow().collectAsState(initial = emptyList())

//    StatusBarColorUpdateEffect(toolbarColor)
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn {
                    items(employees) { employee ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* handle on click to each employee*/ }
                                .border(1.dp, Color.Gray)
                                .padding(16.dp)
                        ) {
                            Text(
                                "${employee.employeeId}: ${employee.firstName + ' ' + employee.lastName}",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                FloatingActionButton(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onTertiaryContainer,
                    onClick = { navigateToEmployeeAdd() },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "add",
                        tint = colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {

}
