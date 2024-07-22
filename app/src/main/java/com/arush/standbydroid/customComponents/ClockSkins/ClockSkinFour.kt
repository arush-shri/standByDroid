package com.arush.standbydroid.customComponents.clockSkins

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arush.standbydroid.R
import com.arush.standbydroid.view.generateRandomClockColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min


@Composable
fun ClockSkinFour(currentTime: String, intervalMinutes: MutableState<Int>, orientation: Int){
    var currentColor by remember { mutableStateOf(Color(0xFFC9BCBA)) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(intervalMinutes.value) {
        coroutineScope.launch {
            while (true) {
                delay(intervalMinutes.value * 60 * 1000L)
                currentColor = generateRandomClockColor()
            }
        }
    }

    var fontSize by remember { mutableStateOf(16.sp) }
    val density = LocalDensity.current.density

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Black).onSizeChanged {
            fontSize = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                (max(it.width, it.height) / density * 0.1).sp
            } else {
                (min(it.width, it.height) / density * 0.1).sp
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,) {
        Row(verticalAlignment = Alignment.CenterVertically)  {
            if(currentTime.substring(9) == "PM"){
                Text(
                    text = ((currentTime.substring(0,2)).toInt() + 12).toString(),
                    style = TextStyle(
                        fontFamily = FontFamily(
                            androidx.compose.ui.text.font.Font(
                                R.font.days28_later,
                            ),
                        ),
                        fontWeight = FontWeight.W900,
                        fontSize = (fontSize*2.5),
                        color = currentColor,
                    ),
                    modifier = Modifier.offset(x = (-20).dp, y = (0).dp)
                )
            }
            else{
                Text(
                    text = currentTime.substring(0,2),
                    style = TextStyle(
                        fontFamily = FontFamily(
                            androidx.compose.ui.text.font.Font(
                                R.font.days28_later,
                            ),
                        ),
                        fontWeight = FontWeight.W900,
                        fontSize = (fontSize*2.8),
                        color = currentColor,
                    ),
                    modifier = Modifier.offset(x = (-20).dp, y = (0).dp)
                )
            }
            Text(
                text = ":",
                style = TextStyle(
                    fontFamily = FontFamily(
                        androidx.compose.ui.text.font.Font(
                            R.font.days28_later,
                        ),
                    ),
                    fontWeight = FontWeight.W900,
                    fontSize = (fontSize*1.5),
                    color = currentColor,
                ),
                modifier = Modifier.offset(x = (-10).dp, y = (0).dp)
            )
            Text(
                text = currentTime.substring(3,5),
                style = TextStyle(
                    fontFamily = FontFamily(
                        androidx.compose.ui.text.font.Font(
                            R.font.days28_later,
                        ),
                    ),
                    fontWeight = FontWeight.W900,
                    fontSize = (fontSize*2.8),
                    color = currentColor,
                ),
                modifier = Modifier.offset(x = (0).dp, y = (0).dp)
            )
            Text(
                text = ":",
                style = TextStyle(
                    fontFamily = FontFamily(
                        androidx.compose.ui.text.font.Font(
                            R.font.days28_later,
                        ),
                    ),
                    fontWeight = FontWeight.W900,
                    fontSize = (fontSize*1.5),
                    color = currentColor,
                ),
                modifier = Modifier.offset(x = (10).dp, y = (0).dp)
            )
            Text(
                text = currentTime.substring(6,8),
                style = TextStyle(
                    fontFamily = FontFamily(
                        androidx.compose.ui.text.font.Font(
                            R.font.days28_later,
                        ),
                    ),
                    fontWeight = FontWeight.W900,
                    fontSize = (fontSize*2.8),
                    color = currentColor,
                ),
                modifier = Modifier.offset(x = (20).dp, y = (0).dp)
            )
        }
    }
}
