package com.example.f23hopper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.f23hopper.ui.nav.NavScreen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier
) {
    // Observe the NavController's back stack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = NavScreen.values().filter {
        it == NavScreen.EmployeeList || it == NavScreen.Calendar
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            val selected = item.route == currentRoute
            if(currentRoute != NavScreen.EmployeeEntry.route||
                currentRoute != NavScreen.EmployeeEdit.route){
                NavigationBarItem(
                    icon = {
                        when (item) {
                            NavScreen.Calendar -> Icon(
                                Icons.Default.DateRange,
                                contentDescription = item.toString()
                            )
                            NavScreen.EmployeeList -> Icon(
                                Icons.Default.AccountBox,
                                contentDescription = item.toString()
                            )
                            else -> {}
                        }
                    },
                    selected = selected,
                    onClick = {
                        if (!selected) {  // prevent re-navigation to the current screen
                            navController.navigate(item.route) {
                                // To clear the back stack to prevent a growing stack
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}
