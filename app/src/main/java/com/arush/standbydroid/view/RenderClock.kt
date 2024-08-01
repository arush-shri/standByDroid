package com.arush.standbydroid.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import com.arush.standbydroid.UserPreferenceManager
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RenderClock(orientation: Int, toggleFullScreen : () -> Unit, modifier: Modifier = Modifier, screenLockCallback : ()->Unit, isLockedScreen : MutableState<Boolean>){
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val intervalMinutes = remember { mutableIntStateOf(UserPreferenceManager.getColorChangeTime(context)) }
    val currentSkinIndex = remember {
        mutableIntStateOf(UserPreferenceManager.getClockSkin(context))
    }
    var pagerState = rememberPagerState (initialPage = currentSkinIndex.intValue)  { 10 }
    var pageSelected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        UserPreferenceManager.saveClockSkin(context, pagerState.currentPage)
        coroutineScope.launch {
            while (true) {
                delay(1000L)
                currentTime = getCurrentTime()
            }
        }
    }

    if (pageSelected) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .background(Color(0xFF5D5985)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Card(
                modifier = Modifier.fillMaxSize(0.9f),
                shape = RoundedCornerShape(30.dp)
            ) {
                VerticalPager(state = pagerState) { currentPage ->
                    DisplaySkin(
                        currentPage = currentPage,
                        currentTime = currentTime,
                        intervalMinutes = intervalMinutes,
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
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 5.dp, horizontal = 25.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if(!isLockedScreen.value) pageSelected = true
                        },
                        onDoubleTap = {
                            toggleFullScreen()
                        }
                    )
                }
        ) {
            DisplaySkin(
                currentPage = pagerState.currentPage,
                currentTime = currentTime,
                intervalMinutes = intervalMinutes,
                orientation = orientation,
                pageSelected = pageSelected,
                callBack = {
                    screenLockCallback()
                }
            )
        }
    }
}
@Composable
private fun DisplaySkin(currentPage: Int, currentTime: String, intervalMinutes: MutableState<Int>, orientation: Int, pageSelected: Boolean, callBack: () -> Unit) {
    when (currentPage) {
        0 -> ClockSkinZero(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        1 -> ClockSkinOne(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        2 -> ClockSkinTwo(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        3 -> ClockSkinThree(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        4 -> ClockSkinFour(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        5 -> ClockSkinFive(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        6 -> ClockSkinSix(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        7 -> ClockSkinSeven(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        8 -> ClockSkinEight(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
        9 -> ClockSkinNine(currentTime = currentTime, intervalMinutes = intervalMinutes, orientation = orientation, pageSelected = pageSelected, callBack = callBack)
    }
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
