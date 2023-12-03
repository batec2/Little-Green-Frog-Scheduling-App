package com.example.f23hopper.ui.employee.timeoff

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.utils.StatusBarColorUpdateEffect

@Composable
fun TimeOffAddScreen(
    navigateToTimeOff: () -> Unit,
    sharedViewModel: TimeOffViewModel,
){
    StatusBarColorUpdateEffect(toolbarColor)
    val coroutineScope = rememberCoroutineScope()
    TimeOffBody(
        sharedViewModel = sharedViewModel,
        onSaveClick = { /*TODO*/ },
        navigateToEmployeeTimeOff = navigateToTimeOff)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOffBody(
    sharedViewModel: TimeOffViewModel,
    onSaveClick: () -> Unit,
    navigateToEmployeeTimeOff: () -> Unit,
){
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {},
            navigationIcon = {
                IconButton(onClick = { navigateToEmployeeTimeOff() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back To list"
                    )
                }
            },
            actions = {
                ElevatedButton(
                    modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                        onSaveClick()
                    }, //enabled = employeeUiState.isEmployeeValid
                ) {
                    Text(text = "Done")
                }
            },
            modifier = Modifier.height(50.dp),
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DatePickerDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ }) {
                
            }
        }
    }
}