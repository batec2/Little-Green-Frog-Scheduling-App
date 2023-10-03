package com.example.f23hopper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.f23hopper.ui.navigation.NavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentDestination = NavScreen.values().find { it.route == currentRoute }
    val colorScheme = MaterialTheme.colorScheme

    val customColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = colorScheme.primaryContainer,
        navigationIconContentColor = colorScheme.onPrimaryContainer,
        titleContentColor = colorScheme.onPrimaryContainer,
        actionIconContentColor = colorScheme.onPrimaryContainer
    )

    TopAppBar(
        title = {
            ProvideTextStyle(value = typography.headlineSmall.copy(fontSize = 18.sp)) {
                Text(currentDestination.toString())
            }
        },
        navigationIcon = {
            if (currentDestination != NavScreen.EmployeeList && currentDestination != NavScreen.Calendar) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = customColors
    )
}
