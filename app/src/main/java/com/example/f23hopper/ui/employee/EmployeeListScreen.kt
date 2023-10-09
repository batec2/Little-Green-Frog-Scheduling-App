package com.example.f23hopper.ui.employee

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.rememberLockOpenRight

@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel = hiltViewModel<EmployeeListViewModel>()
    val employees by viewModel.employees.asFlow().collectAsState(initial = emptyList())

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                EmployeeListItem(employees)

                FloatingActionButton(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onTertiaryContainer,
                    onClick = { navigateToEmployeeAdd() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
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

@Composable
fun EmployeeListItem(
    employees: List<Employee>
){
    LazyColumn {
        items(employees) { employee ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* handle on click to each employee*/ }
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                Column{
                    Row {
                        Text(
                            "${employee.employeeId}: ${employee.firstName + ' ' + employee.lastName}",
                            style = TextStyle(fontSize = 15.sp)
                        )
                        if(employee.canOpen){
                            Icon(imageVector = rememberLockOpenRight(), contentDescription = "Can Open")
                        }
                        if(employee.canClose){
                            Icon(imageVector = rememberLock(), contentDescription = "Can Open")
                        }
                    }
                    Row{
                        val week = listOf<String>(employee.monday.toString(),
                            employee.tuesday.toString(),
                            employee.wednesday.toString(),
                            employee.thursday.toString(),
                            employee.friday.toString())

                        week.forEach { 
                            week -> Text(text = week.first().toString())
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {

}
