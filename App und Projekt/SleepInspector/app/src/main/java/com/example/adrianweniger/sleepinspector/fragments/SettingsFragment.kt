package com.example.adrianweniger.sleepinspector.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.PersonalInfoEditActivity
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.os.Build
import kotlinx.android.synthetic.main.fragment_avg_sleep_borders.view.*
import android.R.id.edit
import android.util.Log
import android.widget.CompoundButton
import com.example.adrianweniger.sleepinspector.activities.ArticleActivity
import com.example.adrianweniger.sleepinspector.notifications.NotificationScheduler
import com.example.adrianweniger.sleepinspector.receivers.AlarmReceiver
import kotlinx.android.synthetic.main.fragment_sleep_time.*


/**
 * Fragment showing Settings
 * handles input: sets notifications, triggers personal data edit form, triggers display of legal texts
 */


 class SettingsFragment : Fragment() {

    val TAG="SETTINGS FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // load view
        var inflated = inflater!!.inflate(R.layout.fragment_settings, container, false)

        // get preferences
        var preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var morningHour = preferences.getInt("morningHour", 8)
        var morningMinute = preferences.getInt("morningMinute", 0)
        var eveningHour = preferences.getInt("eveningHour", 20)
        var eveningMinute = preferences.getInt("eveningMinute", 0)
        var shouldNotify = preferences.getBoolean("shouldNotify", false)

        // set up views
        inflated.evening_notification.setIs24HourView(true)
        inflated.morning_notification.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            inflated.evening_notification.hour = eveningHour
            inflated.evening_notification.minute = eveningMinute
            inflated.morning_notification.hour = morningHour
            inflated.morning_notification.minute = morningMinute
        } else {
            inflated.evening_notification.currentHour = eveningHour
            inflated.evening_notification.currentMinute = eveningMinute
            inflated.morning_notification.currentHour = morningHour
            inflated.morning_notification.currentMinute = morningMinute
        }
        inflated.should_notify.isChecked = shouldNotify
        inflated.personal_info_edit_btn.paintFlags = inflated.personal_info_edit_btn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        inflated.privacy.paintFlags = inflated.privacy.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        //listeners
        inflated.should_notify.setOnCheckedChangeListener({ _, isChecked ->
            val editor = preferences.edit()
            editor.putBoolean("shouldNotify", isChecked)
            editor.commit()
            shouldNotify = isChecked


            if (isChecked){
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, eveningHour, eveningMinute, "evening")
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, morningHour, morningMinute, "morning")
            } else {
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "evening")
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "morning")
            }
        })
        inflated.evening_notification.setOnTimeChangedListener({ _, hour, min ->
            val editor = preferences.edit()
            editor.putInt("eveningHour", hour)
            editor.putInt("eveningMinute", min)
            editor.commit()
            eveningHour = hour
            eveningMinute = min

            Log.d(TAG, "eveningHour: ${preferences.getInt("eveningHour", 20)}")
            Log.d(TAG, "eveningMinute: ${preferences.getInt("eveningMinute", 0)}")

            if (shouldNotify){
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, eveningHour, eveningMinute, "evening")
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, morningHour, morningMinute, "morning")
            } else {
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "evening")
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "morning")
            }
        })
        inflated.morning_notification.setOnTimeChangedListener({ _, hour, min ->
            val editor = preferences.edit()
            editor.putInt("morningHour", hour)
            editor.putInt("morningMinute", min)
            editor.commit()
            morningHour = hour
            morningMinute = min

            Log.d(TAG, "morningHour: ${preferences.getInt("morningHour", 8)}")
            Log.d(TAG, "morningMinute: ${preferences.getInt("morningMinute", 0)}")

            if (shouldNotify){
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, eveningHour, eveningMinute, "evening")
                NotificationScheduler().setReminder(context, AlarmReceiver::class.java, morningHour, morningMinute, "morning")
            } else {
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "evening")
                NotificationScheduler().cancelReminder(context, AlarmReceiver::class.java, "morning")
            }
        })
        inflated.personal_info_edit_btn.setOnClickListener {
            val intent = Intent(context, PersonalInfoEditActivity::class.java)
            startActivity(intent)
        }
        inflated.privacy.setOnClickListener {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra("id", 0)
            context.startActivity(intent)
        }

        return inflated
    }

}