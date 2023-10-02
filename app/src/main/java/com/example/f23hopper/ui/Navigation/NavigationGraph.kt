package com.example.f23hopper.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.employee.EmployeeListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.EmployeeList.route,
        modifier = modifier
    ) {
        composable(route = NavScreen.EmployeeList.route) {
            EmployeeListScreen(
                navigateToEmployeeAdd = { navController.navigate(NavScreen.EmployeeEntry.route) }
            )
        }
        composable(route = NavScreen.EmployeeEntry.route) {
            EmployeeEntryScreen()
        }
    }
}
