package com.arush.standbydroid.customComponents

import androidx.compose.ui.graphics.Color
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