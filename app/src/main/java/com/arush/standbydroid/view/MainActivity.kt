package com.arush.standbydroid.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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

    private lateinit var lockScreenReceiver: BroadcastReceiver
    private var isLockedState = mutableStateOf(false)

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
                HomeScreen(
                    toggleFullScreen = { toggleFullScreen() },
                    isLockedScreen = isLockedState,
                    screenLockCallback = {
                        isLockedState.value = true
                        Toast.makeText(this@MainActivity, "Screen Locked. Press any volume button to unlock screen", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        lockScreenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
                    runOnUiThread {
                        if(isLockedState.value){
                            Toast.makeText(this@MainActivity, "Screen Unlocked", Toast.LENGTH_SHORT).show()
                            isLockedState.value = false
                        }
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction("android.media.VOLUME_CHANGED_ACTION")
        }
        registerReceiver(lockScreenReceiver, filter)
    }

    private fun toggleFullScreen() {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val handler = Handler(Looper.getMainLooper())

        insetsController.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())

        handler.postDelayed({
            insetsController.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        }, 2000) // 2 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(lockScreenReceiver)
    }
}

@Composable
fun HomeScreen(toggleFullScreen : () -> Unit, screenLockCallback : ()->Unit, isLockedScreen : MutableState<Boolean>){
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

    MainScreenLayout(orientation ,toggleFullScreen, screenLockCallback, isLockedScreen)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenLayout(orientation: Int, toggleFullScreen : () -> Unit, screenLockCallback : ()->Unit, isLockedScreen : MutableState<Boolean>) {
    val pagerState = rememberPagerState (initialPage = 1)  { 2 }

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = !isLockedScreen.value
        ) {currentPage ->
            when (currentPage) {
                0 -> SettingScreen()
                1 -> RenderScreenOneLandscape(orientation, toggleFullScreen, screenLockCallback, isLockedScreen)
            }
        }
    }
}

@Composable
private fun RenderScreenOneLandscape(orientation: Int, toggleFullScreen : () -> Unit, screenLockCallback : ()->Unit, isLockedScreen : MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Ensuring each component takes up equal space in the Row
        RenderClock(orientation, toggleFullScreen, Modifier.weight(1.3f), screenLockCallback, isLockedScreen)
        RenderBattery(orientation, toggleFullScreen, Modifier.weight(1f), screenLockCallback, isLockedScreen)
    }
}