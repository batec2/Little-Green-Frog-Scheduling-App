package com.example.f23hopper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.f23hopper.ui.Navigation.AppNavHost
import com.example.f23hopper.ui.employee.EmployeeEntryScreen
import com.example.f23hopper.ui.theme.F23hopperTheme
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

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
    fun SchedulerApp(navController: NavHostController = rememberNavController()){
        AppNavHost(navController = navController)
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        F23hopperTheme {
            Greeting("Android")
        }
    }
}