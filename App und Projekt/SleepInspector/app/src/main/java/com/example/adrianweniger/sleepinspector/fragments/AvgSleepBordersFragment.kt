package com.example.adrianweniger.sleepinspector.fragments


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.RISEditActivity
import kotlinx.android.synthetic.main.fragment_avg_sleep_borders.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */


class AvgSleepBordersFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_avg_sleep_borders, container, false)

        var parentActivity = activity as RISEditActivity


        //set up views
        inflated.avg_sleep_start.setIs24HourView(true)
        inflated.avg_sleep_end.setIs24HourView(true)
        if (parentActivity.risData!!.avgSleepStart != "---") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.avg_sleep_start.hour = parentActivity.risData!!.avgSleepStart.split(":")[0].toInt()
                inflated.avg_sleep_start.minute = parentActivity.risData!!.avgSleepStart.split(":")[1].toInt()
            } else {
                inflated.avg_sleep_start.currentHour = parentActivity.risData!!.avgSleepStart.split(":")[0].toInt()
                inflated.avg_sleep_start.currentMinute = parentActivity.risData!!.avgSleepStart.split(":")[1].toInt()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.avg_sleep_start.hour = 8
                inflated.avg_sleep_start.minute = 0
            } else {
                inflated.avg_sleep_start.currentHour = 9
                inflated.avg_sleep_start.currentMinute = 0
            }
        }

        if (parentActivity.risData!!.avgSleepEnd != "---") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.avg_sleep_end.hour = parentActivity.risData!!.avgSleepEnd.split(":")[0].toInt()
                inflated.avg_sleep_end.minute = parentActivity.risData!!.avgSleepEnd.split(":")[1].toInt()
            } else {
                inflated.avg_sleep_end.currentHour = parentActivity.risData!!.avgSleepEnd.split(":")[0].toInt()
                inflated.avg_sleep_end.currentMinute = parentActivity.risData!!.avgSleepEnd.split(":")[1].toInt()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.avg_sleep_end.hour = 8
                inflated.avg_sleep_end.minute = 0
            } else {
                inflated.avg_sleep_end.currentHour = 9
                inflated.avg_sleep_end.currentMinute = 0
            }
        }



        inflated.avg_sleep_start.setOnTimeChangedListener({ _, hour, min ->
            parentActivity.risData!!.avgSleepStart = hour.toString() + ":" + min.toString()
        })
        inflated.avg_sleep_end.setOnTimeChangedListener({ _, hour, min ->
            parentActivity.risData!!.avgSleepEnd = hour.toString() + ":" + min.toString()
        })

        return inflated

    }




}
