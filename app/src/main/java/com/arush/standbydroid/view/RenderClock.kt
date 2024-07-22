package com.arush.standbydroid.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withSave
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinEight
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinFive
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinFour
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinNine
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinOne
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinSeven
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinSix
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinThree
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinTwo
import com.arush.standbydroid.customComponents.clockSkins.ClockSkinZero
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun RenderClock(orientation: Int){
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

    val intervalMinutes = remember { mutableIntStateOf(15) }

    ClockSkinTwo(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation)
}


fun generateRandomClockColor(): Color {
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

fun DrawScope.drawClockHand(center: Offset, length: Float, color: Color, startWidth: Float, endWidth: Float) {
    val path = Path().apply {
        moveTo(center.x - startWidth / 2, center.y)
        lineTo(center.x + startWidth / 2, center.y)
        lineTo(center.x + endWidth / 2, center.y - length)

        // Create a rounded tip
        arcTo(
            rect = Rect(
                center.x - endWidth / 2,
                center.y - length - endWidth / 2,
                center.x + endWidth / 2,
                center.y - length + endWidth / 2
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        lineTo(center.x - endWidth / 2, center.y - length)
        close()
    }

    val paint = Paint().apply {
        this.color = color
        this.isAntiAlias = true
    }
    drawPath(
        path = path,
        color = color,
        style = Fill,
        alpha = 1.0f,
        blendMode = BlendMode.SrcOver,
    )
    drawCircle(
        color = color,
        radius = startWidth / 2,
        center = center,
        style = Fill,
        alpha = 1.0f,
        blendMode = BlendMode.SrcOver,
    )
}


fun DrawScope.drawClockStarWarsHand(center: Offset, length: Float, color: Color, width: Float) {
    val halfWidth = width * 0.65f
    val path = Path().apply {

        moveTo(center.x - halfWidth, center.y)
        arcTo(
            Rect(center.x - halfWidth, center.y - halfWidth, center.x + halfWidth, center.y + halfWidth),
            180f,
            180f,
            false
        )
        lineTo(center.x + halfWidth, center.y - length + halfWidth)
        arcTo(
            Rect(center.x - halfWidth, center.y - length - halfWidth, center.x + halfWidth, center.y - length + halfWidth),
            0f,
            -180f,
            false
        )
        lineTo(center.x - halfWidth, center.y)

        close()
    }

    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            setShadowLayer(
                22f, 15f, 0f, color.toArgb()
            )
        }
        canvas.nativeCanvas.withSave {
            drawPath(path.asAndroidPath(), paint)
        }
    }

    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            setShadowLayer(
                22f, -15f, 0f, color.toArgb()
            )
        }
        canvas.nativeCanvas.withSave {
            drawPath(path.asAndroidPath(), paint)
        }
    }

    drawPath(path = path, color = color)
    drawCircle(color = color, radius = (width * 0.75).toFloat(), center = center)
}

fun DrawScope.drawHarryPotterWandHand(center: Offset, length: Float, color: Color, baseWidth: Float, tipWidth: Float) {
    val path = Path().apply {
        // Start at the base of the wand
        moveTo(center.x - baseWidth / 2, center.y)
        lineTo(center.x + baseWidth / 2, center.y)

        lineTo(center.x + tipWidth / 2, center.y - length)
        lineTo(center.x - tipWidth / 2, center.y - length)

        close()
    }

    drawPath(
        path = path,
        color = color,
        style = Fill,
        alpha = 1.0f,
        blendMode = BlendMode.SrcOver
    )
    drawCircle(
        color = color,
        radius = baseWidth / 2,
        center = center,
        style = Fill,
        alpha = 1.0f,
        blendMode = BlendMode.SrcOver
    )
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    return sdf.format(Date())
}