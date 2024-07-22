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
import com.arush.standbydroid.view.drawClockHand
import com.arush.standbydroid.view.generateRandomClockColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import com.arush.standbydroid.R

@Composable
fun ClockSkinFive(currentTime: String, intervalMinutes: MutableState<Int>, orientation: Int) {
    val hour = currentTime.substring(0,2).toInt()
    val minute = currentTime.substring(3,5).toInt()
    val second = currentTime.substring(6,8).toInt()

    var currentColor by remember { mutableStateOf(Color(0xFFFF6C40)) }
    var currentHourColor by remember { mutableStateOf(Color(0xFFF14949)) }
    var currentMinuteColor by remember { mutableStateOf(Color(0xFF4EA303)) }
    var currentSecondColor by remember { mutableStateOf(Color(0xFFFF9800)) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val customFont = ResourcesCompat.getFont(context, R.font.playwritehrlijeva)

    LaunchedEffect(intervalMinutes.value) {
        coroutineScope.launch {
            while (true) {
                delay(intervalMinutes.value * 60 * 1000L)
                currentColor = generateRandomClockColor()
                currentHourColor = generateRandomClockColor()
                currentMinuteColor = generateRandomClockColor()
                currentSecondColor = generateRandomClockColor()
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
                val textHeight = (radius * 0.07).sp.toPx()
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
                            textSize = textHeight
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                            typeface = customFont
                        }
                    )
                }

                rotate(degrees = hour * 30f + minute * 0.5f, pivot = center) {
                    drawClockHand(center, radius * 0.50f, currentHourColor, 16f, 8f)
                }

                rotate(degrees = minute * 6f + second * 0.1f, pivot = center) {
                    drawClockHand(center, radius * 0.65f, currentMinuteColor, 16f, 8f)
                }

                rotate(degrees = second * 6f, pivot = center) {
                    drawClockHand(center, radius * 0.75f, currentSecondColor, 16f, 8f)
                }
            }
        }
    }
}
