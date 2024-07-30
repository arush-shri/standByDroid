package com.arush.standbydroid.view

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arush.standbydroid.UserPreferenceManager
import com.arush.standbydroid.customComponents.batterySkins.BatterySkinZero
import com.arush.standbydroid.listeners.PowerConnectionReceiver
import com.arush.standbydroid.listeners.getBatteryPercent
import com.arush.standbydroid.listeners.getChargingStatus
import com.arush.standbydroid.listeners.startBatteryListener
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RenderBattery(orientation: Int, toggleFullScreen : () -> Unit){
    val context = LocalContext.current
    var isBatterySwitchChecked by remember { mutableStateOf(UserPreferenceManager.getBatteryAccess(context)) }
    var batteryStatus by remember { mutableStateOf<Intent?>(null) }
    val intervalMinutes = remember { mutableIntStateOf(UserPreferenceManager.getColorChangeTime(context)) }
    var isCharging by remember { mutableStateOf(false) }
    var batteryPct by remember { mutableStateOf<Float?>(100f) }
    val currentSkinIndex = remember { mutableIntStateOf(UserPreferenceManager.getBatterySkin(context)) }
    var pagerState = rememberPagerState (initialPage = currentSkinIndex.intValue)  { 10 }
    var pageSelected by remember { mutableStateOf(false) }

    DisposableEffect(isBatterySwitchChecked) {
        batteryStatus = startBatteryListener(context)
        val receiver = PowerConnectionReceiver { chargingStatus, batteryPercentage ->
            isCharging = chargingStatus
            batteryPct = batteryPercentage
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED).apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        if (isBatterySwitchChecked) {
            context.registerReceiver(receiver, filter)
        }

        onDispose {
            if (isBatterySwitchChecked) {
                context.unregisterReceiver(receiver)
            }
        }
    }


    if (pageSelected) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF5D5985)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Card(
                modifier = Modifier.fillMaxSize(0.9f),
                shape = RoundedCornerShape(30.dp)
            ) {
                VerticalPager(state = pagerState) { currentPage ->
                    displaySkin(
                        currentPage = pagerState.currentPage,
                        intervalMinutes = intervalMinutes,
                        chargingStatus = isCharging,
                        chargingPercentage = batteryPct,
                        orientation = orientation,
                        pageSelected = pageSelected,
                        callBack = {
                            pageSelected = false
                            UserPreferenceManager.saveClockSkin(context, currentPage)
                        }
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {
                        pageSelected = true
                    }, onDoubleTap = {
                        toggleFullScreen()
                    })
                }
        ) {
            displaySkin(
                currentPage = pagerState.currentPage,
                intervalMinutes = intervalMinutes,
                chargingStatus = isCharging,
                chargingPercentage = batteryPct,
                orientation = orientation,
                pageSelected = pageSelected,
                callBack = {}
            )
        }
    }
}

@Composable
private fun displaySkin(currentPage: Int, intervalMinutes: MutableState<Int>, chargingStatus: Boolean, chargingPercentage: Float?, orientation: Int, pageSelected: Boolean, callBack: () -> Unit){
    BatterySkinZero(
        currentPage = currentPage,
        intervalMinutes = intervalMinutes,
        chargingStatus = chargingStatus,
        chargingPercentage = chargingPercentage,
        orientation = orientation,
        pageSelected = pageSelected
    ) {

    }
}

fun generateRandomBatteryColor(): Color {
    val random = Random
    var red: Float
    var green: Float
    var blue: Float

    do {
        red = random.nextFloat()
        green = random.nextFloat()
        blue = random.nextFloat()

    } while (red < 0.5f || green < 0.5f || blue < 0.5f)
    return Color(
        red = red,
        green = green,
        blue = blue,
        alpha = 1f
    )
}