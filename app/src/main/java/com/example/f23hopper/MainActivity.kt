package com.example.f23hopper


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.F23HopperTheme
import com.example.f23hopper.data.HopperDatabase
import com.example.f23hopper.ui.components.BottomNavigationBar
import com.example.f23hopper.ui.nav.AppNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var db: HopperDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            // NOTE: This will wipe the DB and then populate it with dummy fields.
            // Remove this in PROD
            activateDemoDatabase(db)
        }
        setContent {
            F23HopperTheme {
                SplashScreenTransition()
            }
        }
    }
}

@Composable
fun SplashScreenTransition() {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // PERF: put data loading logic here
        delay(2000)
        showSplash = false
    }

    Crossfade(targetState = showSplash, label = "") { isSplashVisible ->
        if (isSplashVisible) {
            FullScreenSplash()
        } else {
            SchedulerApp()
        }
    }
}

@Composable
fun FullScreenSplash() {

    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "F23Hopper Scheduler",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun SchedulerApp(navController: NavHostController = rememberNavController()) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val colorScheme = MaterialTheme.colorScheme
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
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



