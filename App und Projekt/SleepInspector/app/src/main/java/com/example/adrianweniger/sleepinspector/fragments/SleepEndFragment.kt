package com.example.adrianweniger.sleepinspector.fragments

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import kotlinx.android.synthetic.main.fragment_sleep_end.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class SleepEndFragment : Fragment(){

    val TAG="SLEEP END FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_sleep_end, container, false)
        var parentActivity = activity as MorningEditActivity

        //setup views
        inflated.sleep_end_picker.setIs24HourView(true)
        inflated.out_of_bed_picker.setIs24HourView(true)

        if (parentActivity.morningData!!.sleepEnd != "---") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.sleep_end_picker.hour = parentActivity.morningData!!.sleepEnd.split(":")[0].toInt()
                inflated.sleep_end_picker.minute = parentActivity.morningData!!.sleepEnd.split(":")[1].toInt()
            } else {
                inflated.sleep_end_picker.currentHour = parentActivity.morningData!!.sleepEnd.split(":")[0].toInt()
                inflated.sleep_end_picker.currentMinute = parentActivity.morningData!!.sleepEnd.split(":")[1].toInt()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.sleep_end_picker.hour = 8
                inflated.sleep_end_picker.minute = 0
            } else {
                inflated.sleep_end_picker.currentHour = 9
                inflated.sleep_end_picker.currentMinute = 0
            }
        }
        if (parentActivity.morningData!!.outOfBedTime != "---") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.out_of_bed_picker.hour = parentActivity.morningData!!.outOfBedTime.split(":")[0].toInt()
                inflated.out_of_bed_picker.minute = parentActivity.morningData!!.outOfBedTime.split(":")[1].toInt()
            } else {
                inflated.out_of_bed_picker.currentHour = parentActivity.morningData!!.outOfBedTime.split(":")[0].toInt()
                inflated.out_of_bed_picker.currentMinute = parentActivity.morningData!!.outOfBedTime.split(":")[1].toInt()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inflated.out_of_bed_picker.hour = 8
                inflated.out_of_bed_picker.minute = 0
            } else {
                inflated.out_of_bed_picker.currentHour = 9
                inflated.out_of_bed_picker.currentMinute = 0
            }
        }


        //listeners
        inflated.sleep_end_picker.setOnTimeChangedListener({ _, hour, min ->
            parentActivity.morningData!!.sleepEnd = hour.toString() + ":" + min.toString()
        })
        inflated.out_of_bed_picker.setOnTimeChangedListener({ _, hour, min ->
            parentActivity.morningData!!.outOfBedTime = hour.toString() + ":" + min.toString()
        })

        return inflated
    }
}