package com.example.f23hopper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.f23hopper.ui.calendar.CalendarScreen
import com.example.f23hopper.ui.employee.EmployeeEditScreen
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.employee.EmployeeListScreen
import com.example.f23hopper.ui.employee.EmployeeListViewModel
import com.example.f23hopper.ui.shiftedit.ShiftEditScreen
import kotlinx.datetime.LocalDate

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<EmployeeListViewModel>()
    NavHost(
        navController = navController,
        startDestination = NavScreen.Calendar.route,
        modifier = modifier
    ) {
        //sub graph
        navigation(
            route = NavScreen.SharedEmployeeList.route,
            startDestination = NavScreen.EmployeeList.route
        ){
            composable(route = NavScreen.EmployeeList.route) {
                EmployeeListScreen(
                    //For button to go to Entry screen from the list
                    navigateToEmployeeAdd =
                    {
                        navController.navigate(NavScreen.EmployeeEntry.route)
                    },
                    navigateToEmployeeEdit =
                    {
                        navController.navigate(NavScreen.EmployeeEdit.route)
                    },
                    sharedViewModel = viewModel
                )
            }
            composable(route = NavScreen.EmployeeEdit.route) {
                EmployeeEditScreen(
                    //For button to go back to the List screen when done editing
                    navigateToEmployeeList =
                    {
                        //navController.navigate(NavScreen.EmployeeList.route)
                        navController.popBackStack()
                    },
                    sharedViewModel = viewModel
                )
            }
            composable(route = NavScreen.EmployeeEntry.route) {
                EmployeeEntryScreen(
                    //For button to go back to list from entry screen
                    navigateToEmployeeList = {
                        //navController.navigate(NavScreen.SharedEmployeeList.route)
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(route = NavScreen.Calendar.route) {
            CalendarScreen(
                navigateToShiftView = { clickedDay -> navController.navigate("${NavScreen.ScheduleEdit.route}/$clickedDay") }
            )
        }
        composable("${NavScreen.ScheduleEdit.route}/{date}") { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date").toString())
            ShiftEditScreen(clickedDay = date, navController)
        }
    }
}


