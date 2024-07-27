package com.arush.standbydroid.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.Toast

class PowerConnectionReceiver(
    private val onBatteryStatusChanged: (isCharging: Boolean, batteryPct: Float?) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (action == Intent.ACTION_BATTERY_CHANGED || action == Intent.ACTION_POWER_CONNECTED || action == Intent.ACTION_POWER_DISCONNECTED) {
                val isCharging = getChargingStatus(intent)
                val batteryPct = getBatteryPercent(intent)
                onBatteryStatusChanged(isCharging, batteryPct)
            }
        }
    }

    private fun getChargingStatus(batteryStatus: Intent?): Boolean {
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    private fun getBatteryPercent(batteryStatus: Intent?): Float? {
        return batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level != -1 && scale != -1) {
                level * 100 / scale.toFloat()
            } else {
                null
            }
        }
    }
}

fun startBatteryListener(context : Context) : Intent?{
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        context.registerReceiver(null, ifilter)
    }
    return batteryStatus
}

fun getChargingStatus(batteryStatus: Intent?) : Boolean{
    val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL

    return isCharging
}

fun getBatteryPercent(batteryStatus: Intent?) : Float? {
    val batteryPct: Float? = batteryStatus?.let { intent ->
        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    }

    return batteryPct
}