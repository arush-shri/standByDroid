package com.arush.standbydroid.customComponents.clockSkins

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import com.arush.standbydroid.R
import com.arush.standbydroid.customComponents.drawClockStarWarsHand

@Composable
fun ClockSkinSix(currentTime: String, intervalMinutes: MutableState<Int>) {
    val hour = currentTime.substring(0,2).toInt()
    val minute = currentTime.substring(3,5).toInt()
    val second = currentTime.substring(6,8).toInt()

    var currentColor by remember { mutableStateOf(Color(0xFFFFE81F)) }
    var currentHourColor by remember { mutableStateOf(Color(0xFF2FF924)) }
    var currentMinuteColor by remember { mutableStateOf(Color(0xFFEB212E)) }
    var currentSecondColor by remember { mutableStateOf(Color(0xFF2E67F8)) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val customFont = ResourcesCompat.getFont(context, R.font.star_wars)

    LaunchedEffect(intervalMinutes.value) {
        coroutineScope.launch {
            while (true) {
                delay(intervalMinutes.value * 60 * 1000L)
                currentColor = getRandomSaberColor(currentColor)
                currentHourColor = getRandomSaberColor(currentHourColor)
                currentMinuteColor = getRandomSaberColor(currentMinuteColor)
                currentSecondColor = getRandomSaberColor(currentSecondColor)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas { canvas ->
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.minDimension / 2

                drawCircle(
                    color = Color.Transparent,
                    radius = radius,
                    center = center
                )

                val numberRadius = radius * 0.85f
                val textHeight = 40.sp.toPx()
                for (number in 1..12) {
                    val angle = (number - 3) * (2 * PI / 12).toFloat()
                    val x = centerX + numberRadius * cos(angle)
                    val y = centerY + numberRadius * sin(angle) + textHeight / 4
                    canvas.nativeCanvas.drawText(
                        number.toString(),
                        x,
                        y,
                        Paint().asFrameworkPaint().apply {
                            color = currentColor.toArgb()
                            textSize = 40.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                            typeface = customFont
                        }
                    )
                }

                rotate(degrees = hour * 30f + minute * 0.5f, pivot = center) {
                    drawClockStarWarsHand(center, radius * 0.55f, currentHourColor, 10f)
                }

                rotate(degrees = minute * 6f + second * 0.1f, pivot = center) {
                    drawClockStarWarsHand(center, radius * 0.70f, currentMinuteColor, 10f)
                }

                rotate(degrees = second * 6f, pivot = center) {
                    drawClockStarWarsHand(center, radius * 0.80f, currentSecondColor, 10f)
                }
            }
        }
    }
}

private fun getRandomSaberColor(excludeColor: Color): Color {
    val availableColors = listOf(
        Color(0xFF871EFE),
        Color(0xFF2FF924),
        Color(0xFF2E67F8),
        Color(0xFFFFE81F),
        Color(0xFFEB212E),
        Color(0xFFFF8425),
        Color(0xFFFFFFFF),
        Color(0xFFCCFF00),
        Color(0xFFFF81F3),
        Color(0xFF3F00FF),
    )
    val filteredColors = availableColors.filter { it != excludeColor }
    return filteredColors.random()
}