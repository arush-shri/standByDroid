package com.arush.standbydroid.customComponents

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withSave
import kotlin.random.Random

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
        lineTo(center.x - endWidth / 2, center.y - length)
        close()
    }
    drawPath(path = path, color = color)
    drawCircle(color = color, radius = startWidth / 2, center = center)
}

fun DrawScope.drawClockStarWarsHand(center: Offset, length: Float, color: Color, width: Float) {
    val path = Path().apply {
        moveTo(center.x - width / 2, center.y)
        lineTo(center.x + width / 2, center.y)
        quadraticBezierTo(center.x + width*0.8f, center.y - length + width / 2, center.x, center.y - length)
        quadraticBezierTo(center.x - width*0.8f, center.y - length + width / 2, center.x - width / 2, center.y)
        close()
    }
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            setShadowLayer(
                22f, 15f,0f, color.toArgb()
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
                22f, -15f,0f, color.toArgb()
            )
        }
        canvas.nativeCanvas.withSave {
            drawPath(path.asAndroidPath(), paint)
        }
    }

    drawPath(path = path, color = color)
    drawCircle(color = color, radius = (width*0.7).toFloat(), center = center)
}