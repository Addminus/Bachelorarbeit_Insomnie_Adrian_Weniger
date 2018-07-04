@file:Suppress("DEPRECATION")

package com.example.adrianweniger.sleepinspector.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.*
import android.app.NotificationManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.EveningEditActivity
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity


/**
 * Used to schedule, cancel and show notifications
 * uses the andriod alarm system to schedule notifications
 * broadcasted notifications are then handled by the alarm Receiver
 * Alarm Receiver triggers .showNotification() to display Notifiation on Screen
 */
class NotificationScheduler {

    // set up request codes for notifications
    val MORNING_REMINDER_REQUEST_CODE=100001
    val EVENING_REMINDER_REQUEST_CODE=100002

    private val TAG="NOTIFICATION SCHEDULER"

    // set notification for evening odr morning
    fun setReminder(context:Context, cls: Class<*>, hour: Int, min: Int, tag:String) {

        val now = Calendar.getInstance()

        var scheduledTime = Calendar.getInstance()
        scheduledTime.set(Calendar.HOUR_OF_DAY, hour)
        scheduledTime.set(Calendar.MINUTE, min)
        scheduledTime.set(Calendar.SECOND, 0)

        // cancel already scheduled notifications
        cancelReminder(context,cls, tag)
        if(scheduledTime.before(now)){
            Log.d(TAG, "Day added")
            scheduledTime.add(Calendar.DATE,1)

        }

        var intent = Intent(context, cls)
        intent.putExtra("tag", tag)
        if(tag == "morning"){
            var pendingIntent = PendingIntent.getBroadcast(context, MORNING_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            var alarm = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, scheduledTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else{
            var pendingIntent = PendingIntent.getBroadcast(context, EVENING_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            var alarm = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, scheduledTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }

        Log.d(TAG, "set ${tag} Reminder for ${hour} : ${min}")
    }



    fun cancelReminder(context: Context, cls: Class<*>, tag:String) {

        val intent1 = Intent(context, cls)

        if(tag == "morning"){
            val pendingIntent = PendingIntent.getBroadcast(context, MORNING_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarm = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.cancel(pendingIntent)
            pendingIntent.cancel()
        } else{
            val pendingIntent = PendingIntent.getBroadcast(context, EVENING_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarm = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.cancel(pendingIntent)
            pendingIntent.cancel()
        }

    }


    // show notifications on screen. add intent -> click on notification opens appropriate Edit Activity
    fun showNotification(context: Context, tag:String) {

        if(tag == "morning"){
            val intent = Intent(context, MorningEditActivity::class.java)
            intent.putExtra("date", System.currentTimeMillis())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            var pendingIntent = PendingIntent.getActivity(context, MORNING_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            var mBuilder = NotificationCompat.Builder(context)
            val notification = mBuilder
                    .setContentTitle("SleepInspector")
                    .setContentText("Haben Sie gut geschlafen?")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setContentIntent(pendingIntent)
                    .build()
            mBuilder.priority = NotificationCompat.PRIORITY_MAX
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(MORNING_REMINDER_REQUEST_CODE, notification)
            Log.d(TAG, "showing morning notification")
        } else{
            val intent = Intent(context, EveningEditActivity::class.java)
            intent.putExtra("date", System.currentTimeMillis())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            var pendingIntent = PendingIntent.getActivity(context, EVENING_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            var mBuilder = NotificationCompat.Builder(context)
            val notification = mBuilder
                    .setContentTitle("SleepInspector")
                    .setContentText("Wie ging es Ihnen heute?")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setContentIntent(pendingIntent)
                    .build()
            mBuilder.priority = NotificationCompat.PRIORITY_MAX
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(EVENING_REMINDER_REQUEST_CODE, notification)
            Log.d(TAG, "showing evening notification")
        }
    }
}