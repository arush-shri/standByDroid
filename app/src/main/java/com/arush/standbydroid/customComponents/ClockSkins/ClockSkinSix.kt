package com.arush.standbydroid.customComponents.clockSkins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.arush.standbydroid.ui.theme.StandByDroidTheme
import com.arush.standbydroid.ui.theme.provider


@Composable
fun ClockSkinSix(currentTime: String){
    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center) {
        Text(
            text = currentTime,
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(
                        googleFont = GoogleFont("Lobster Two"),
                        fontProvider = provider,
                        weight = FontWeight.Bold,
                        style = FontStyle.Italic
                    )
                ),
                fontSize = 25.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSix() {
    StandByDroidTheme {
        ClockSkinZero(currentTime = "9:11:00")
    }
}

