package com.arush.StandByDroid

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        // optional: log new notifications
        // Log.d("MyNotificationListener", "Notification posted: $sbn")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // Log.d("MyNotificationListener", "Notification removed: $sbn")
    }
}
