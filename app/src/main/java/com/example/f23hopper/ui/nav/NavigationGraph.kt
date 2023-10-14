package com.example.f23hopper.ui.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f23hopper.ui.calendar.CalendarScreen
import com.example.f23hopper.ui.calendar.WeekViewScreen
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.employee.EmployeeListScreen
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.Calendar.route,
        modifier = modifier
    ) {
        composable(route = NavScreen.EmployeeList.route) {
            EmployeeListScreen(
                navigateToEmployeeAdd = { navController.navigate(NavScreen.EmployeeEntry.route) }
            )
        }
        composable(route = NavScreen.EmployeeEntry.route) {
            EmployeeEntryScreen(
                navigateToEmployeeList = {navController.navigate(NavScreen.EmployeeList.route)}
            )
        }
        composable(route = NavScreen.Calendar.route) {
            CalendarScreen(
                navigateToDayView = { clickedDay -> navController.navigate("${NavScreen.CalendaWeekView.route}/$clickedDay") }
            )
        }
        composable("${NavScreen.CalendaWeekView.route}/{date}") { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date").toString())
            WeekViewScreen(clickedDay = date)
        }
    }
}
