package com.example.f23hopper.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.employee.EmployeeListScreen

@Composable
fun AppNavHost(
    navController:NavHostController
){
    NavHost(
        navController = navController,
        startDestination = "EmployeeList"
    ) {
        composable(route = "EmployeeList"){
            EmployeeListScreen(
                navigateToEmployeeAdd = {navController.navigate("EmployeeEntry")}
            )
        }
        composable(route = "EmployeeEntry"){
            EmployeeEntryScreen()
        }
    }
}