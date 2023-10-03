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
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.ui.components.BottomNavigationBar
import com.example.f23hopper.ui.components.TopAppBar
import com.example.f23hopper.ui.nav.AppNavHost
import com.example.f23hopper.ui.theme.F23hopperTheme
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
            populateDatabase(db)
        }

        setContent {

            F23hopperTheme {
                SchedulerApp()
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