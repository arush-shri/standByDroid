package com.arush.standbydroid.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arush.standbydroid.ui.theme.StandByDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
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

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape layout
        LandscapeLayout(orientation)
    } else {
        // Portrait layout
        PortraitLayout(orientation)
    }
}

@Composable
fun PortraitLayout(orientation: Int) {
    Column(Modifier.fillMaxSize()) {
        RenderClock(orientation)
    }
}

@Composable
fun LandscapeLayout(orientation: Int) {
    Column(Modifier.fillMaxSize()) {
        RenderClock(orientation)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StandByDroidTheme {
        HomeScreen()
    }
}