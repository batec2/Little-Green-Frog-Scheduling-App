package com.example.f23hopper


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.f23hopper.ui.nav.AppNavHost
import com.example.f23hopper.ui.components.BottomNavigationBar
import com.example.f23hopper.ui.components.TopAppBar
import com.example.f23hopper.ui.theme.F23hopperTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            F23hopperTheme {
                SchedulerApp()
                /*
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Kalendar(
                            currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                            kalendarType = KalendarType.Firey,
                            modifier = Modifier,
                        )
                    }
                     */
            }
        }
    }

    @Composable
    fun SchedulerApp(navController: NavHostController = rememberNavController()) {
        var selectedItem by remember { mutableStateOf(0) }
        val colorScheme = MaterialTheme.colorScheme

        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                TopAppBar(navController)
                AppNavHost(navController = navController, modifier = Modifier.weight(6f))
                BottomNavigationBar(
                    navController,
                    colorScheme,
                    Modifier.weight(1f),
                    selectedItem
                ) { newIndex ->
                    selectedItem = newIndex
                }
            }
        }
    }


}