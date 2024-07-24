package com.arush.standbydroid

import android.content.Context
import android.content.SharedPreferences

object UserPreferenceManager {
    private const val PREFS_NAME = "StandByDroidPreferences"
    private const val KEY_CLOCK_SKIN = "key_clock_skin"
    private const val KEY_CALENDAR_ACCESS = "key_calendar_skin"
    private const val KEY_NOTIFICATION_ACCESS = "key_notification_skin"
    private const val KEY_PICTURE_ACCESS = "key_picture_skin"

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
}