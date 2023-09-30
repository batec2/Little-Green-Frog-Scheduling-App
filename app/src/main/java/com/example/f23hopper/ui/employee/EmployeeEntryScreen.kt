package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryScreen(

){
    var text by remember{ mutableStateOf("") }
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
                value = text,
                onValueChange = {text = it},
                label = { Text("First Name") }
            )
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = { Text("Last Name") }
            )
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = { Text("Phone Number") }
            )


        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    EmployeeEntryScreen()
}