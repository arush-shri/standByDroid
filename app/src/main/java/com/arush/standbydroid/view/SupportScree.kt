package com.arush.standbydroid.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SupportScreen(orientation: Int){
    if(orientation == Configuration.ORIENTATION_LANDSCAPE){
        LandscapeView()
    }
    else{
        PortraitView()
    }
}

@Composable
private fun LandscapeView(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Support")
    }
}

@Composable
private fun PortraitView(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Support")
    }
}