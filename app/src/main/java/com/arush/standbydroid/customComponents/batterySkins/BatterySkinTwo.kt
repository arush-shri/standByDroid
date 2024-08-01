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
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
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
fun BatterySkinTwo(intervalMinutes: MutableState<Int>,
                   chargingStatus: Boolean, chargingPercentage: Float?, orientation: Int, pageSelected: Boolean, callBack: () -> Unit) {
    var currentColor by remember { mutableStateOf(Color(0xFF95E251)) }
    var fontSize by remember { mutableStateOf(16.sp) }
    val density = LocalDensity.current.density
    val context = LocalContext.current
    val customFont = ResourcesCompat.getFont(context, R.font.formula1_bold)
    val lightningBolt = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_bolt))

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
            val sweepAngle = (chargingPercentage?.div(100))?.times(360)

            val rectSize = min(canvasWidth, canvasHeight)
            val rectOffsetX = (canvasWidth - rectSize) * 1.5f
            val rectOffsetY = (canvasHeight - rectSize) / 2

            if (sweepAngle != null) {
                drawArc(
                    color = currentColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = if(orientation == Configuration.ORIENTATION_LANDSCAPE) 36f else 20f,
                        cap = StrokeCap.Round
                    ),
                    topLeft = Offset(rectOffsetX, rectOffsetY),
                    size = Size(rectSize/2.5f, rectSize/2.5f)
                )
            }

            if(chargingStatus){
                translate(left = rectOffsetX + rectSize/22, top = rectOffsetY + rectSize/22) {
                    with(lightningBolt){
                        draw(
                            size = Size(rectSize/3.5f, rectSize/3.5f),
                            colorFilter = ColorFilter.tint(color = currentColor)
                        )
                    }
                }
            }

            drawIntoCanvas { canvas->
                if (chargingPercentage != null) {
                    canvas.nativeCanvas.drawText(
                        chargingPercentage.toInt().toString(),
                        canvasWidth * 0.7f,
                        canvasHeight / if(orientation == Configuration.ORIENTATION_LANDSCAPE) 1.25f else 1.8f,
                        Paint().asFrameworkPaint().apply {
                            color = currentColor.toArgb()
                            textSize = (rectSize/11).dp.toPx()
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
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(onClick = {
                    callBack()
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Done", tint = currentColor)
                }
            }
        }
    }
}