package com.arush.standbydroid.customComponents.batterySkins

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.arush.standbydroid.R
import com.arush.standbydroid.view.generateRandomBatteryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@Composable
fun BatterySkinZero(intervalMinutes: MutableState<Int>,
                    chargingStatus: Boolean, chargingPercentage: Float?, orientation: Int, pageSelected: Boolean, callBack: () -> Unit) {
    var currentColor by remember { mutableStateOf(Color(0xFF95E251)) }
    var fontSize by remember { mutableStateOf(16.sp) }
    val density = LocalDensity.current.density
    val infiniteTransition = rememberInfiniteTransition(label = "BatteryAnimation")
    val context = LocalContext.current
    val customFont = ResourcesCompat.getFont(context, R.font.batmfilled)
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "BatteryAnimation"
    )

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(intervalMinutes.value) {
        coroutineScope.launch {
            while (true) {
                delay(intervalMinutes.value * 60 * 1000L)
                currentColor = generateRandomBatteryColor()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .onSizeChanged {
                fontSize = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    (max(it.width, it.height) / density * 0.1).sp
                } else {
                    (min(it.width, it.height) / density * 0.1).sp
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val rectSize = min(canvasWidth, canvasHeight) / 1.5f
            val rectWidth = rectSize * 1.3f
            val rectHeight = rectSize / 2.2f
            val rectOffsetX = (canvasWidth - rectWidth) / 2
            val rectOffsetY = (canvasHeight - rectHeight) / 2

            drawRoundRect(
                color = currentColor,
                topLeft = Offset(rectOffsetX, rectOffsetY),
                size = Size(rectWidth, rectHeight),
                cornerRadius = CornerRadius(rectSize / 10, rectSize / 10),
                style = Stroke(
                    width = 10f
                )
            )

            val innerRectWidth = (rectWidth - 10.dp.toPx())
            val innerRectHeight = rectHeight - 12.dp.toPx()
            val innerRectOffsetX = rectOffsetX + 5.dp.toPx()
            val innerRectOffsetY = rectOffsetY + 6.dp.toPx()

            val partWidth = innerRectWidth / 5
            var i = 0
            while ( chargingPercentage != null && i < (chargingPercentage.div(20)).toInt()) {
                val partOffsetX = innerRectOffsetX + i * partWidth
                drawRoundRect(
                    color = currentColor,
                    topLeft = Offset(partOffsetX, innerRectOffsetY),
                    size = Size(partWidth - if(i==4) 0.dp.toPx() else 2.dp.toPx(), innerRectHeight),
                    cornerRadius = CornerRadius(innerRectHeight / 6, innerRectHeight / 6)
                )
                i++
            }
            if( chargingPercentage != null && chargingPercentage.toInt() < 100 && !chargingStatus){
                drawRoundRect(
                    color = currentColor,
                    topLeft = Offset((innerRectOffsetX + i * partWidth),innerRectOffsetY),
                    size = Size(partWidth * (chargingPercentage - (i*20))/20, innerRectHeight),
                    cornerRadius = CornerRadius(innerRectHeight / 6, innerRectHeight / 6)
                )
            }
            else if( chargingPercentage != null && chargingPercentage.toInt() < 100){
                drawRoundRect(
                    color = Color(0xFFF86622),
                    topLeft = Offset((innerRectOffsetX + i * partWidth),innerRectOffsetY),
                    size = Size(partWidth * animatedFloat, innerRectHeight),
                    cornerRadius = CornerRadius(innerRectHeight / 7 * animatedFloat, innerRectHeight / 7 * animatedFloat)
                )
            }
            drawIntoCanvas { canvas->
                if (chargingPercentage != null) {
                    canvas.nativeCanvas.drawText(
                        chargingPercentage.toInt().toString(),
                        canvasWidth / 2,
                        (canvasHeight + rectHeight + 60.dp.toPx()) / 2,
                        Paint().asFrameworkPaint().apply {
                            color = currentColor.toArgb()
                            textSize = 36.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                            typeface = customFont
                        }
                    )
                }
            }
        }
        if(pageSelected){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(onClick = {
                    callBack()
                },
                    colors = ButtonDefaults.buttonColors(containerColor = currentColor)
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
        }
    }
}