package com.example.adrianweniger.sleepinspector.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import kotlinx.android.synthetic.main.fragment_sleep_start.view.*
import kotlinx.android.synthetic.main.fragment_sleep_time.view.*
import kotlin.math.min

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class SleepTimeFragment :Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
       //load view
        var inflated = inflater!!.inflate(R.layout.fragment_sleep_time, container, false)
        var parentActivity = activity as MorningEditActivity
        //set up needed vars
        var hourString = ""
        var minuteString = ""
        var hoursInMinutes = 0
        var minutes = 0

        // set up views
        if(parentActivity.morningData!!.sleepTimeString != "---"){
            hourString = parentActivity.morningData!!.sleepTimeString.split(",")[0].dropLast(4)
            minuteString = parentActivity.morningData!!.sleepTimeString.split(",")[1].dropLast(4)

            hoursInMinutes = parentActivity.morningData!!.sleepTimeString.split(",")[0].dropLast(4).toInt() * 60
            minutes = parentActivity.morningData!!.sleepTimeString.split(",")[1].dropLast(4).toInt()
            inflated.sleep_time_hour_input.setText(hourString, TextView.BufferType.EDITABLE)
            inflated.sleep_time_minutes_input.setText(minuteString, TextView.BufferType.EDITABLE)
        }


        //listeners
        inflated.sleep_time_hour_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                    hoursInMinutes = 0
                } else {
                    hoursInMinutes = number.toString().toInt()*60
                    parentActivity.morningData!!.sleepTime = hoursInMinutes + minutes

                    hourString = number.toString()
                    parentActivity.morningData!!.sleepTimeString = "${hourString} Std,${minuteString} min"
                }
            }
        })

        inflated.sleep_time_minutes_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                    minutes = 0
                } else {
                    minutes = number.toString().toInt()
                    parentActivity.morningData!!.sleepTime = hoursInMinutes + minutes

                    minuteString = number.toString()
                    parentActivity.morningData!!.sleepTimeString = "${hourString} Std,${minuteString} min"

                }
            }
        })
        return inflated
    }
}