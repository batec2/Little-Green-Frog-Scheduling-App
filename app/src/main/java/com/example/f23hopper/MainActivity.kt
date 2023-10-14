package com.example.f23hopper


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.F23HopperTheme
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.ui.components.BottomNavigationBar
import com.example.f23hopper.ui.nav.AppNavHost
import com.example.f23hopper.utils.isDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var db: HopperDatabase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        GlobalScope.launch {
            // NOTE: This will wipe the DB and then populate it with dummy fields.
            // Remove this in PROD
            activateDemoDatabase(db)
        }

        setContent {

            F23HopperTheme {
                SchedulerApp()
            }
        }
    }

    @Composable
    fun SchedulerApp(navController: NavHostController = rememberNavController()) {
        var selectedItem by remember { mutableIntStateOf(0) }
        val colorScheme = MaterialTheme.colorScheme
        val systemUiController = rememberSystemUiController()
        val isDarkTheme = isDark()
        // Set navigation bar color and icon colors
        SideEffect {
            systemUiController.setNavigationBarColor(
                color = colorScheme.surface,
                darkIcons = !isDarkTheme
            )
            systemUiController.setSystemBarsColor(
                color = colorScheme.surface,
                darkIcons = !isDarkTheme
            )
        }


        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                AppNavHost(navController = navController, modifier = Modifier.weight(6f))
                BottomNavigationBar(
                    navController,
                    Modifier.weight(.4f)
                )
            }
        }
    }

}