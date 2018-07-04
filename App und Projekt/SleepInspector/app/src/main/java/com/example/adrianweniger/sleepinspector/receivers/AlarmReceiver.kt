package com.example.adrianweniger.sleepinspector.receivers

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import com.example.adrianweniger.sleepinspector.notifications.NotificationScheduler

/**
 * receiver handling triggered Notifications scheduled by the NotificationScheduler
 *
 */

class AlarmReceiver : BroadcastReceiver() {

    val TAG="ALARM RECEIVER"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received Alarm")

        val tag = intent!!.getStringExtra("tag")

        NotificationScheduler().showNotification(context!!, tag)
    }

}
