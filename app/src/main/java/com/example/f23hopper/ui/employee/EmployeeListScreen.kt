package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmployeeListScreen(

){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {/*Navigate to Add page*/}) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        }
    ){innerPadding->
        Text(text = "Hello World")
    }
}


@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {
    EmployeeListScreen()
}