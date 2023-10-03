package com.example.f23hopper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.f23hopper.ui.navigation.NavScreen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    colorScheme: ColorScheme,
    modifier: Modifier,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = NavScreen.values().filter {
        it == NavScreen.EmployeeList || it == NavScreen.Calendar
    }
    NavigationBar(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        NavScreen.EmployeeList -> Icon(
                            Icons.Default.AccountBox,
                            contentDescription = item.toString()
                        )

                        NavScreen.Calendar -> Icon(
                            Icons.Default.DateRange,
                            contentDescription = item.toString()
                        )

                        else -> {}
                    }
                },
                label = { Text(item.toString()) },
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                    navController.navigate(item.route)
                }
            )
        }
    }
}
