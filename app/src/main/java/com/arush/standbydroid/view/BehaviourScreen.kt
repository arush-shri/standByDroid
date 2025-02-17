package com.arush.standbydroid.view

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arush.standbydroid.UserPreferenceManager

@Composable
fun BehaviourScreen(orientation: Int){
    BehaviourScreenView(orientation)
}

@Composable
private fun BehaviourScreenView(orientation: Int){
    val context = LocalContext.current
    var colorChangeTime by remember { mutableIntStateOf(UserPreferenceManager.getColorChangeTime(context)) }
    var isShowOnChargingSwitchChecked by remember { mutableStateOf(UserPreferenceManager.getShowOnChargingPreference(context)) }
    var sliderPosition by remember { mutableFloatStateOf((colorChangeTime - 5) / (60f - 5f)) }
    var isStartLandscapeSwitchChecked by remember { mutableStateOf(UserPreferenceManager.getStartLandscapeMode(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier.padding(start = 20.dp, top = 30.dp, end = 20.dp)
                } else {
                    Modifier.padding(start = 0.dp, top = 0.dp, end = 0.dp)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Color Change Duration",
                style = TextStyle(
                    fontSize = 20.sp
                ),
                color = Color.White
            )
            Text(
                text = "To protect your screen from burning app automatically changes view color",
                style = TextStyle(
                    fontSize = 14.sp
                ),
                color = Color(0xFF586F81)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.93f)
                ) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                            colorChangeTime = (it * (60 - 5) + 5).toInt()
                            UserPreferenceManager.saveColorChangeTime(context, colorChangeTime)},
                        valueRange = 0f..1f,
                        steps = 0
                    )
                }
                Text(
                    text = "$colorChangeTime",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 5.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.then(
                    if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                        Modifier
                    }
                    else{
                        Modifier.fillMaxWidth(0.8f)
                    }
                )
            ) {
                Text(
                    text = "Show on Charging",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Turn this setting on to see stand by screen on charging",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isShowOnChargingSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        isShowOnChargingSwitchChecked = true
                        UserPreferenceManager.saveShowOnChargingPreference(context, true)
                    } else {
                        isShowOnChargingSwitchChecked = false
                        UserPreferenceManager.saveShowOnChargingPreference(context, false)
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 15.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.then(
                    if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                        Modifier
                    }
                    else{
                        Modifier.fillMaxWidth(0.8f)
                    }
                )
            ) {
                Text(
                    text = "Use app in landscape mode",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Turn on to use app in landscape mode. If off then app uses device orientation",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isStartLandscapeSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        isStartLandscapeSwitchChecked = true
                        UserPreferenceManager.saveStartLandscapeMode(context, true)
                    } else {
                        isStartLandscapeSwitchChecked = false
                        UserPreferenceManager.saveStartLandscapeMode(context, false)
                    }
                    Toast.makeText(context, "Restart the app to see changes", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
