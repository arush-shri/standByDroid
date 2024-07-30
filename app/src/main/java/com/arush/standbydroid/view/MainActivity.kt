package com.arush.standbydroid.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        setContent {
            StandByDroidTheme {
                HomeScreen(toggleFullScreen = { toggleFullScreen() })
            }
        }
    }

    private fun toggleFullScreen() {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val handler = Handler(Looper.getMainLooper())

        insetsController.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())

        handler.postDelayed({
            insetsController.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        }, 2000) // 2 seconds
    }
}

@Composable
fun HomeScreen(toggleFullScreen : () -> Unit){
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val context = LocalContext.current
    val currentView = LocalView.current

    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    MainScreenLayout(orientation ,toggleFullScreen)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenLayout(orientation: Int, toggleFullScreen : () -> Unit) {
    val pagerState = rememberPagerState (initialPage = 1)  { 2 }

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) {currentPage ->
            when (currentPage) {
                0 -> SettingScreen()
                1 -> RenderScreenOneLandscape(orientation, toggleFullScreen)
            }
        }
    }
}

@Composable
private fun RenderScreenOneLandscape(orientation: Int, toggleFullScreen : () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Ensuring each component takes up equal space in the Row
        RenderClock(orientation, toggleFullScreen, Modifier.weight(1f))
        RenderBattery(orientation, toggleFullScreen, Modifier.weight(1f))
    }
}