package com.arush.standbydroid.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinEight
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinFive
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinFour
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinOne
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinSeven
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinSix
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinThree
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinTwo
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinZero
import com.arush.standbydroid.ui.theme.StandByDroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StandByDroidTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(){
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                delay(1000L)
                currentTime = getCurrentTime()
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape layout
        LandscapeLayout(currentTime)
    } else {
        // Portrait layout
        PortraitLayout(currentTime)
    }
}

@Composable
fun PortraitLayout(currentTime: String) {
    val intervalMinutes = remember { mutableIntStateOf(1) }
    Column(Modifier.fillMaxSize()) {
        ClockSkinEight(currentTime, intervalMinutes)
    }
}

@Composable
fun LandscapeLayout(currentTime: String) {
    Column(Modifier.fillMaxSize()) {

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StandByDroidTheme {
        HomeScreen()
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    return sdf.format(Date())
}