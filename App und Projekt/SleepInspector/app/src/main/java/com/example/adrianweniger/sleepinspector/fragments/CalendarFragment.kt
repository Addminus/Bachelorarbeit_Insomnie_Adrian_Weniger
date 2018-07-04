package com.example.adrianweniger.sleepinspector.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.EveningEditActivity
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import com.example.adrianweniger.sleepinspector.activities.RISEditActivity
import com.example.adrianweniger.sleepinspector.database.database
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.*
import java.text.SimpleDateFormat

/**
 * Fragment showing Calendar
 * entry point to create Sleepdiary or RIS entries
 */

 class CalendarFragment : Fragment() {

    val TAG="CALENDAR FRAGMENT"

    var monthYear = SimpleDateFormat("MMM yyyy", Locale.GERMAN)
    var selectedDate = Date()




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
       //load view
        var inflated = inflater!!.inflate(R.layout.fragment_calendar, container, false )


        // transforming colors from R.color to android.graphics.Color as the compactCalendar library only accepts those!
        val primaryDarkColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorPrimaryDark)    } else {
            resources.getColor(R.color.colorPrimaryDark)
        }

        val eveningColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorEvening)    } else {
            resources.getColor(R.color.colorEvening)
        }

        val morningColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorMorning)    } else {
            resources.getColor(R.color.colorMorning)
        }

        val risColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.gray)    } else {
            resources.getColor(R.color.gray)
        }


        //calendar set up
        inflated.compact_calendar.setLocale(TimeZone.getDefault(), Locale.GERMAN)
        inflated.compact_calendar.setUseThreeLetterAbbreviation(true)
        inflated.compact_calendar.setCurrentDayBackgroundColor(primaryDarkColor)
        inflated.monthDisplay.text = monthYear.format(inflated.compact_calendar.firstDayOfCurrentMonth)


        //floating action button set up
        inflated.floating_action_button_menu.hideMenu(false)

        // view setup
        inflated.morning_state_img.visibility = View.GONE
        inflated.evening_state_img.visibility = View.GONE
        inflated.ris_state_img.visibility = View.GONE
        inflated.morning_state_img.setImageDrawable(resources.getDrawable(R.drawable.checked))
        inflated.evening_state_img.setImageDrawable(resources.getDrawable(R.drawable.checked))
        inflated.ris_state_img.setImageDrawable(resources.getDrawable(R.drawable.checked))

        // get data from DB
        var morningDataList = activity.applicationContext.database.getAllMorningData()
        var eveningDataList = activity.applicationContext.database.getAllEveningData()
        var risDataList = activity.applicationContext.database.getAllRISData()

        //create Events for Calendar
        for(morning in morningDataList){
            val data = "morning"
            val event = Event(morningColor, morning.dateInMillis, data)
            inflated.compact_calendar.addEvent(event, false)
        }
        for(evening in eveningDataList){
            val data = "evening"
            var event = Event(eveningColor, evening.dateInMillis, data)
            inflated.compact_calendar.addEvent(event, false)
        }
        for(risData in risDataList){
            val data = "ris"
            var event = Event(risColor, risData.dateInMillis, data)
            inflated.compact_calendar.addEvent(event, false)
        }


        // listeners
        inflated.compact_calendar.setListener(object: CompactCalendarView.CompactCalendarViewListener {
            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                inflated.monthDisplay.text = monthYear.format(firstDayOfNewMonth!!)
            }

            override fun onDayClick(dateClicked:Date) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)

                selectedDate = dateClicked
                //only show edit Button if selected date before current date
                //prevent filling out entries in the future
                if(cal.timeInMillis > dateClicked.time) {
                    inflated.floating_action_button_menu.showMenu(false)

                } else {
                    inflated.floating_action_button_menu.hideMenu(false)
                }

                inflated.morning_state_img.visibility = View.GONE
                inflated.evening_state_img.visibility = View.GONE
                inflated.ris_state_img.visibility = View.GONE

                // show if entries exists
                for (event in inflated.compact_calendar.getEvents(dateClicked)){
                    when(event.data){
                        "morning" -> inflated.morning_state_img.visibility = View.VISIBLE
                        "evening" -> inflated.evening_state_img.visibility = View.VISIBLE
                        "ris" -> inflated.ris_state_img.visibility = View.VISIBLE
                    }
                }
            }
        })

        //floating action button listeners
        inflated.fab_edit_morning.setOnClickListener {
            val intent = Intent(context, MorningEditActivity::class.java)
            intent.putExtra("date", selectedDate.time)
            startActivity(intent)

        }
        inflated.fab_edit_evening.setOnClickListener {
            val intent = Intent(context, EveningEditActivity::class.java)
            intent.putExtra("date", selectedDate.time)
            startActivity(intent)
        }

        inflated.fab_edit_ris.setOnClickListener {
            val intent = Intent(context, RISEditActivity::class.java)
            intent.putExtra("date", selectedDate.time)
            startActivity(intent)
        }

        return inflated
    }
}
