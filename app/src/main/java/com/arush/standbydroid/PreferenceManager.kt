package com.arush.standbydroid

import android.content.Context
import android.content.SharedPreferences

object UserPreferenceManager {
    private const val PREFS_NAME = "StandByDroidPreferences"
    private const val KEY_CLOCK_SKIN = "key_clock_skin"
    private const val KEY_CALENDAR_ACCESS = "key_calendar_access"
    private const val KEY_NOTIFICATION_ACCESS = "key_notification_access"
    private const val KEY_PICTURE_ACCESS = "key_picture_skin"
    private const val KEY_BATTERY_ACCESS = "key_battery_access"
    private const val KEY_BATTERY_SKIN = "key_battery_skin"
    private const val KEY_COLOR_CHANGE_TIME = "key_color_change_time"
    private const val KEY_SHOW_ON_CHARGING = "key_show_on_charging"
    private const val KEY_START_LANDSCAPE = "key_start_landscape"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveClockSkin(context: Context, skinIndex: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_CLOCK_SKIN, skinIndex)
        editor.apply()
    }
    fun getClockSkin(context: Context): Int {
        return getPreferences(context).getInt(KEY_CLOCK_SKIN, 0)
    }

    fun saveColorChangeTime(context: Context, changedTime: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_COLOR_CHANGE_TIME, changedTime)
        editor.apply()
    }
    fun getColorChangeTime(context: Context): Int {
        return getPreferences(context).getInt(KEY_COLOR_CHANGE_TIME, 5)
    }

    fun saveCalendarAccess(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_CALENDAR_ACCESS, isEnabled)
        editor.apply()
    }
    fun getCalendarAccess(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_CALENDAR_ACCESS, false)
    }

    fun saveNotificationAccess(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_NOTIFICATION_ACCESS, isEnabled)
        editor.apply()
    }
    fun getNotificationAccess(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_NOTIFICATION_ACCESS, false)
    }

    fun savePictureAccess(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_PICTURE_ACCESS, isEnabled)
        editor.apply()
    }
    fun getPictureAccess(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_PICTURE_ACCESS, false)
    }

    fun saveBatteryAccess(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_BATTERY_ACCESS, isEnabled)
        editor.apply()
    }
    fun getBatteryAccess(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_BATTERY_ACCESS, false)
    }

    fun saveShowOnChargingPreference(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_SHOW_ON_CHARGING, isEnabled)
        editor.apply()
    }
    fun getShowOnChargingPreference(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_SHOW_ON_CHARGING, false)
    }

    fun saveBatterySkin(context: Context, skinIndex: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_BATTERY_SKIN, skinIndex)
        editor.apply()
    }
    fun getBatterySkin(context: Context): Int {
        return getPreferences(context).getInt(KEY_BATTERY_SKIN, 0)
    }

    fun saveStartLandscapeMode(context: Context, isEnabled: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_START_LANDSCAPE, isEnabled)
        editor.apply()
    }
    fun getStartLandscapeMode(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_START_LANDSCAPE, true)
    }
}