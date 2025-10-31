package com.arush.StandByDroid

import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.media.MediaMetadata
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import android.app.NotificationManager
import android.provider.Settings
import android.content.Intent
import android.util.Log
import android.content.ComponentName

@ReactModule(name = MediaControllerModule.NAME)
class MediaControllerModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "MediaControllerModule"
    }

    private val mediaSessionManager: MediaSessionManager =
        reactContext.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

    private val controllers: List<MediaController>?
        get() = try {
            val componentName = ComponentName(
                reactApplicationContext,
                MyNotificationListener::class.java
            )
            mediaSessionManager.getActiveSessions(componentName)
        } catch (e: SecurityException) {
            null
        }

    override fun getName(): String = NAME

    fun isNotificationAccessGranted(context: Context): Boolean {
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = context.packageName
        return enabledListeners != null && enabledListeners.contains(packageName)
    }

    /** ✅ Get currently playing media info */
    @ReactMethod
    fun getNowPlaying(promise: Promise) {
        try {
            if (!isNotificationAccessGranted(reactApplicationContext)) {
                promise.resolve("denied")
                return
            }
            val sessionControllers = controllers
            if (sessionControllers.isNullOrEmpty()) {
                promise.resolve("none")
                return
            }

            val controller = sessionControllers.first()
            val metadata = controller.metadata
            val state = controller.playbackState

            if (metadata == null) {
                promise.resolve(null)
                return
            }

            if (metadata != null) {
                val keys = metadata.keySet()
                Log.d("MediaControllerModule", "MediaMetadata:")
                for (key in keys) {
                    val value = metadata.getString(key) ?: metadata.getLong(key).toString()
                    Log.d("MediaControllerModule", "$key : $value")
                }
            } else {
                Log.d("MediaControllerModule", "Metadata is null")
            }

            val map = Arguments.createMap().apply {
                putString("title", metadata.getString(MediaMetadata.METADATA_KEY_TITLE))
                putString("artist", metadata.getString(MediaMetadata.METADATA_KEY_ARTIST))
                putString("album", metadata.getString(MediaMetadata.METADATA_KEY_ALBUM))
                putString("artwork", metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI))
                putDouble("position", state?.position?.toDouble() ?: 0.0)
                putDouble("duration", metadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble())
                putInt("state", state?.state ?: PlaybackState.STATE_NONE)
            }

            promise.resolve(map)
        } catch (e: Exception) {
            promise.reject("ERR_NOW_PLAYING", e)
        }
    }

    @ReactMethod
    fun openNotificationAccessSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    /** ✅ Playback controls: play, pause, next, prev */
    @ReactMethod
    fun sendCommand(action: String, promise: Promise) {
        try {
            val sessionControllers = controllers
            if (sessionControllers.isNullOrEmpty()) {
                promise.reject("NO_SESSION", "No active sessions")
                return
            }

            val controller = sessionControllers.first()
            val controls = controller.transportControls

            when (action.lowercase()) {
                "play" -> controls.play()
                "pause" -> controls.pause()
                "next" -> controls.skipToNext()
                "prev", "previous" -> controls.skipToPrevious()
                else -> {
                    promise.reject("INVALID_ACTION", "Unknown action: $action")
                    return
                }
            }
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERR_COMMAND", e)
        }
    }

    /** ✅ Get playback position + duration */
    @ReactMethod
    fun getPlaybackPosition(promise: Promise) {
        try {
            val sessionControllers = controllers
            if (sessionControllers.isNullOrEmpty()) {
                promise.reject("NO_SESSION", "No active sessions")
                return
            }

            val controller = sessionControllers.first()
            val state = controller.playbackState
            val metadata = controller.metadata

            val map = Arguments.createMap().apply {
                putDouble("position", state?.position?.toDouble() ?: 0.0)
                putDouble("duration", metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)?.toDouble() ?: 0.0)
                putInt("state", state?.state ?: PlaybackState.STATE_NONE)
            }

            promise.resolve(map)
        } catch (e: Exception) {
            promise.reject("ERR_PLAYBACK_POS", e)
        }
    }

    /** ✅ Seek to a specific position (milliseconds) */
    @ReactMethod
    fun seekTo(position: Double, promise: Promise) {
        try {
            val sessionControllers = controllers
            if (sessionControllers.isNullOrEmpty()) {
                promise.reject("NO_SESSION", "No active sessions")
                return
            }

            val controller = sessionControllers.first()
            controller.transportControls.seekTo(position.toLong())
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERR_SEEK", e)
        }
    }
}
