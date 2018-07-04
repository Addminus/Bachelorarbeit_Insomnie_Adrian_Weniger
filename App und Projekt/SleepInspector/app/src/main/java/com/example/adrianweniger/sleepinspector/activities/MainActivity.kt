package com.example.adrianweniger.sleepinspector.activities

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.adrianweniger.sleepinspector.ArticleLoader
import com.example.adrianweniger.sleepinspector.PDFCreator
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.database.database
import com.example.adrianweniger.sleepinspector.fragments.*
import com.example.adrianweniger.sleepinspector.notifications.NotificationScheduler
import com.example.adrianweniger.sleepinspector.receivers.AlarmReceiver

/**
 * main entry point to application
 * displays main views via fragments and navigation to get to them
 * loads data needed in other fragments/activities
 */


class MainActivity : AppCompatActivity() {

    private val TAG = "MAIN ACTIVITY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ArticleLoader.loadArticles(this)

        if(database.getPersonalInfo() == null){
            supportActionBar!!.hide()
            showFragment(DisclaimerFragment())
        } else {

            val inflater = LayoutInflater.from(this)
            val actionBar = inflater.inflate(R.layout.action_bar, null)
            //Center the textview in the ActionBar
            val params = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER)
            supportActionBar!!.setCustomView(actionBar, params)
            supportActionBar!!.setDisplayShowCustomEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)

            initMainView()
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val morningHour = preferences.getInt("morningHour", 8)
        val morningMinutes = preferences.getInt("morningMinute", 0)
        val eveningHour = preferences.getInt("eveningHour", 20)
        val eveningMinutes = preferences.getInt("eveningMinute", 0)
        val shouldNotify = preferences.getBoolean("shouldNotify", false)

        if(shouldNotify){
            NotificationScheduler().setReminder(this, AlarmReceiver::class.java, morningHour, morningMinutes, "morning")
            NotificationScheduler().setReminder(this, AlarmReceiver::class.java, eveningHour, eveningMinutes, "evening")
        } else {
            NotificationScheduler().cancelReminder(this, AlarmReceiver::class.java, "morning")
            NotificationScheduler().cancelReminder(this, AlarmReceiver::class.java, "evening")
        }

    }

    //initializes the main view/navigation
    private fun initMainView (){

        val item1 = AHBottomNavigationItem("Kalender", R.drawable.ic_date_range_black_24dp)
        val item2 = AHBottomNavigationItem("Auswertung", R.drawable.ic_equalizer_black_24dp)
        val item3 = AHBottomNavigationItem("Ratgeber", R.drawable.ic_chrome_reader_mode_black_24dp)
        val item4 = AHBottomNavigationItem("Einstellungen", R.drawable.ic_settings_black_24dp)

        var bottomNavigation = findViewById<View>(R.id.bottom_navigation) as? AHBottomNavigation
        bottomNavigation!!.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)
        bottomNavigation.addItem(item4)
        bottomNavigation.accentColor = Color.BLACK
        bottomNavigation.inactiveColor = Color.LTGRAY
        bottomNavigation.isColored = false
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW


        bottomNavigation.setOnTabSelectedListener { pos, _ ->
            when (pos) {
                0 -> showFragment(CalendarFragment())
                1 -> showFragment(ReportFragment())
                2 -> showFragment(GuideFragment())
                3 -> showFragment(SettingsFragment())

                else -> true
            }
        }
        bottomNavigation.setCurrentItem(0, false)
        showFragment(CalendarFragment())
    }


    private fun showFragment(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
        return true
    }



}