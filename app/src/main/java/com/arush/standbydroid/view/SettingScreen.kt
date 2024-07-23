package com.arush.standbydroid.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.arush.standbydroid.R

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
    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.2f).background(color = Color(
            0xFF0F0F0F
        )
        ),
            contentAlignment = Alignment.TopStart) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TabViewButton(buttonText = "Permission") {

                }
                TabViewButton(buttonText = "Behaviour") {

                }
                TabViewButton(buttonText = "Customize") {

                }
                TabViewButton(buttonText = "Support") {

                }
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(color = Color(0xFF101111))) {
            
        }
    }
}

@Composable
fun PortraitSettingLayout(orientation: Int){
    val buttons = listOf("Permission", "Behaviour", "Customize", "Support")

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxHeight(0.08f).fillMaxWidth().background(color = Color(0xFF0F0F0F)
        ),
            contentAlignment = Alignment.TopCenter) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(buttons) { buttonText ->
                    TabViewButton(buttonText = buttonText) {
                        // Add your onClick action here
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(color = Color(0xFF101111))) {

        }
    }
}

@Composable
fun TabViewButton(buttonText : String, callbackFunc : ()->Unit){
    Button(
        onClick = { callbackFunc() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF575858)),
        ) {
        Text(
            text = buttonText,
            style = TextStyle(
                fontSize = 22.sp,
            ),
            color = Color.White
        )
    }
}