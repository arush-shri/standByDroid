package com.arush.StandByDroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // We only take action if the device has just finished booting
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, ChargerService::class.java)
            // Use ContextCompat to handle different Android versions correctly
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}