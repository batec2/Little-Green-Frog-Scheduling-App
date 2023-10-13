package com.example.f23hopper.ui.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.rememberLockOpenRight
import com.example.f23hopper.ui.icons.rememberPartlyCloudyNight
import com.example.f23hopper.ui.icons.rememberWbSunny

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    navigateToEmployeeAdd: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel = hiltViewModel<EmployeeListViewModel>()
    val employees by viewModel.employees.asFlow().collectAsState(initial = emptyList())

//    StatusBarColorUpdateEffect(toolbarColor)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = {},
                actions = {
                    Icon(imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Filter",
                        modifier = Modifier,

                    )
                },
                
            )
        }
                ,
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
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            "${employee.employeeId}: ${employee.firstName + ' ' + employee.lastName}",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        if(employee.canOpen){
                            Icon(imageVector = rememberLockOpenRight(),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Can Open")
                        }
                        if(employee.canClose){
                            Icon(imageVector = rememberLock(),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Can Open")
                        }
                    }
                    Row{
                        val week = listOf(
                            Pair(employee.sunday,"S"),
                            Pair(employee.monday,"M"),
                            Pair(employee.tuesday,"T"),
                            Pair(employee.wednesday,"W"),
                            Pair(employee.thursday,"R"),
                            Pair(employee.friday,"F"),
                            Pair(employee.sunday,"S"))

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Icon(imageVector = rememberWbSunny(),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Day Shift")
                            week.forEach {
                                    week -> Text(text = if(week.first==ShiftType.DAY||week.first==ShiftType.FULL) week.second else "",
                                color = Color.DarkGray)
                            }
                        }
                        Spacer(modifier = Modifier.size(5.dp))
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Icon(imageVector = rememberPartlyCloudyNight(),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Night Shift")
                            week.forEach {
                                    week -> Text(text = if(week.first==ShiftType.NIGHT||week.first==ShiftType.FULL) week.second else " ",
                                color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialogue(

){

}
@Preview(showBackground = true)
@Composable
private fun EmployeeListScreenPreview() {

}
