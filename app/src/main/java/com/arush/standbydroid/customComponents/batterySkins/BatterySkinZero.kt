package com.arush.standbydroid.customComponents.batterySkins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.arush.standbydroid.view.generateRandomBatteryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BatterySkinZero(currentPage: Int, intervalMinutes: MutableState<Int>,
                    chargingStatus: Boolean, chargingPercentage: Float?, orientation: Int, pageSelected: Boolean, callBack: () -> Unit) {
    var currentColor by remember { mutableStateOf(Color(0xFFFF6C40)) }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(intervalMinutes.value) {
        coroutineScope.launch {
            while (true) {
                delay(intervalMinutes.value * 60 * 1000L)
                currentColor = generateRandomBatteryColor()
            }
        }
    }
}