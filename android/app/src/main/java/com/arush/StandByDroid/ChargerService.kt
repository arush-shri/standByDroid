package com.arush.StandByDroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.util.Log

class ChargerService : Service() {

    private lateinit var powerReceiver: BroadcastReceiver
    private val TAG = "ChargerService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created and receiver is being registered.")
        // Register power receiver dynamically
        powerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, "✅ Received Call")
                if (intent.action == Intent.ACTION_POWER_CONNECTED) {
                    val launchIntent = context.packageManager
                        .getLaunchIntentForPackage(context.packageName)
                    launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d(TAG, "✅ Power connected! Done Launching MainActivity...")
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(powerReceiver, filter)

        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "charger_service_channel"
        val channelName = "Charger Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification =
            Notification.Builder(this, channelId)
                .setContentTitle("StandByDroid running")
                .setContentText("Listening for charger events")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34+
            startForeground(1, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
