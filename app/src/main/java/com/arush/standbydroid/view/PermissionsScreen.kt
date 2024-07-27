package com.arush.standbydroid.view

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.core.content.ContextCompat
import com.arush.standbydroid.UserPreferenceManager
import com.arush.standbydroid.listeners.PowerConnectionReceiver
import com.arush.standbydroid.listeners.startBatteryListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PermissionsScreen(orientation: Int){
    SettingPermissionView(orientation)
}

@Composable
private fun SettingPermissionView(orientation: Int){
    val context = LocalContext.current
    var isSwitchChecked by remember { mutableStateOf(UserPreferenceManager.getCalendarAccess(context)) }
    var isNotificationSwitchChecked by remember { mutableStateOf(UserPreferenceManager.getNotificationAccess(context)) }
    var isPictureSwitchChecked by remember { mutableStateOf(UserPreferenceManager.getPictureAccess(context)) }
    var isBatterySwitchChecked by remember { mutableStateOf(UserPreferenceManager.getBatteryAccess(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ){
            isPictureSwitchChecked = true
            UserPreferenceManager.savePictureAccess(context, true)
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            UserPreferenceManager.saveCalendarAccess(context, true)
            isSwitchChecked = true
        }
    }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = "Calendar access",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Please provide access to your calendar to get events and reminders on the app",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_CALENDAR
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            UserPreferenceManager.saveCalendarAccess(context, true)
                            isSwitchChecked = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.READ_CALENDAR)
                        }
                    } else {
                        isSwitchChecked = false
                        UserPreferenceManager.saveCalendarAccess(context, false)
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = "Notification access",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Please provide access to your notifications to see them on the app",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isNotificationSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        isNotificationSwitchChecked = true
                        UserPreferenceManager.saveNotificationAccess(context, true)
                    } else {
                        isNotificationSwitchChecked = false
                        UserPreferenceManager.saveNotificationAccess(context, false)
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = "Picture access",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Please provide access to use your pictures as background images",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isPictureSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_MEDIA_IMAGES
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            UserPreferenceManager.savePictureAccess(context, true)
                            isPictureSwitchChecked = true
                        } else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                            else{
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    } else {
                        isPictureSwitchChecked = false
                        UserPreferenceManager.savePictureAccess(context, false)
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = "Battery status access",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Please provide access to your battery status to see on the app",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF586F81)
                )
            }

            Switch(
                checked = isBatterySwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        isBatterySwitchChecked = true
                        UserPreferenceManager.saveBatteryAccess(context, true)
                    } else {
                        isBatterySwitchChecked = false
                        UserPreferenceManager.saveBatteryAccess(context, false)
                    }
                }
            )
        }
    }
}
