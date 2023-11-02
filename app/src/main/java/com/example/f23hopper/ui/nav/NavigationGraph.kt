package com.example.f23hopper.ui.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.f23hopper.ui.calendar.CalendarScreen
import com.example.f23hopper.ui.employee.EmployeeEditScreen
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.employee.EmployeeListScreen
import com.example.f23hopper.ui.employee.EmployeeListViewModel
import com.example.f23hopper.ui.shiftedit.ShiftEditScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<EmployeeListViewModel>()
    AnimatedNavHost(
        navController = navController,
        startDestination = NavScreen.Calendar.route,
        modifier = modifier
    ) {
        // sub graph
        navigation(
            route = NavScreen.SharedEmployeeList.route,
            startDestination = NavScreen.EmployeeList.route
        ) {
            composable(
                route = NavScreen.EmployeeList.route,
                enterTransition = { slideInHorizontally() + fadeIn() },
                exitTransition = { slideOutHorizontally() + fadeOut() }
            ) {
                EmployeeListScreen(
                    navigateToEmployeeAdd = {
                        navController.navigate(NavScreen.EmployeeEntry.route)
                    },
                    navigateToEmployeeEdit = {
                        navController.navigate(NavScreen.EmployeeEdit.route)
                    },
                    sharedViewModel = viewModel
                )
            }
            composable(
                route = NavScreen.EmployeeEdit.route,
                enterTransition = { slideInHorizontally() + fadeIn() },
                exitTransition = { slideOutHorizontally() + fadeOut() }
            ) {
                EmployeeEditScreen(
                    navigateToEmployeeList = {
                        navController.popBackStack()
                    },
                    sharedViewModel = viewModel
                )
            }
            composable(
                route = NavScreen.EmployeeEntry.route,
                enterTransition = { slideInHorizontally() + fadeIn() },
                exitTransition = { slideOutHorizontally() + fadeOut() }
            ) {
                EmployeeEntryScreen(
                    navigateToEmployeeList = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            route = NavScreen.Calendar.route,
            enterTransition = { slideInHorizontally() + fadeIn() },
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ) {
            CalendarScreen(
                navigateToShiftView = { clickedDay -> navController.navigate("${NavScreen.ScheduleEdit.route}/$clickedDay") }
            )
        }
        composable(
            "${NavScreen.ScheduleEdit.route}/{date}",
            enterTransition = { slideInHorizontally() + fadeIn() },
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ) { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date").toString())
            ShiftEditScreen(clickedDay = date, navController)
        }
    }
}
