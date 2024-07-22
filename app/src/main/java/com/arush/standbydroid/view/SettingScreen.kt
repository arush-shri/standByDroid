package com.arush.standbydroid.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun SettingScreen(){
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape layout
        LandscapeSettingLayout(orientation)
    } else {
        // Portrait layout
        PortraitSettingLayout(orientation)
    }
}

@Composable
fun LandscapeSettingLayout(orientation: Int){
    Column(modifier = Modifier.fillMaxSize().background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,) {

        Text(text = "Setting Coming Soon",
            style = TextStyle(
                fontSize = 28.sp,
            ),
            color = Color.Cyan
        )
    }
}

@Composable
fun PortraitSettingLayout(orientation: Int){
    Column(modifier = Modifier.fillMaxSize().background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,) {

        Text(text = "Setting Coming Soon",
            style = TextStyle(
                fontSize = 28.sp,
            ),
            color = Color.Cyan
        )
    }
}