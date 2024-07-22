package com.arush.standbydroid

import android.content.Context
import android.content.SharedPreferences

object UserPreferenceManager {
    private const val PREFS_NAME = "ClockSkinPreference"
    private const val KEY_CLOCK_SKIN = "key_clock_skin"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveClockSkin(context: Context, skinIndex: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_CLOCK_SKIN, skinIndex)
        editor.apply()
    }

    fun getClockSkin(context: Context): Int {
        return getPreferences(context).getInt(KEY_CLOCK_SKIN, 0) // default to 0
    }
}