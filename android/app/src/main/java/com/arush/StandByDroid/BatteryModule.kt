package com.arush.StandByDroid

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap

class BatteryModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "BatteryModule"
    }

    @ReactMethod
    fun getBatteryInfo(promise: Promise) {
        try {
            val batteryManager = reactApplicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            
            // Get battery current using the standard property
            val current = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)

            // Get battery voltage from the ACTION_BATTERY_CHANGED intent
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus: Intent? = reactApplicationContext.registerReceiver(null, iFilter)
            val voltage = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) ?: -1

            val map: WritableMap = WritableNativeMap()
            map.putInt("voltage", voltage)
            map.putInt("current", current)

            promise.resolve(map)
        } catch (e: Exception) {
            promise.reject("BATTERY_INFO_ERROR", "Failed to get battery info", e)
        }
    }
}