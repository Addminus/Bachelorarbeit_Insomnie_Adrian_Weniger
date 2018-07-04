package com.example.adrianweniger.sleepinspector.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import kotlinx.android.synthetic.main.fragment_sleep_start.view.*
import kotlinx.android.synthetic.main.fragment_times_awake_during_night.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class TimesAwakeDuringNightFragment : Fragment(){

    val TAG="TIMES AWAKE FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load views
        var inflated = inflater!!.inflate(R.layout.fragment_times_awake_during_night, container, false)

        var parentActivity = activity as MorningEditActivity

        // set default / pre-existing data
        inflated.times_awake_input.setText(parentActivity.morningData!!.timesAwakeDuringNight.toString(), TextView.BufferType.EDITABLE)
        inflated.total_time_awake_input.setText(parentActivity.morningData!!.totalTimeAwakeDuringNight.toString(), TextView.BufferType.EDITABLE)

        // listeners
        inflated.times_awake_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                    parentActivity.morningData!!.timesAwakeDuringNight = 0
                } else {
                    parentActivity.morningData!!.timesAwakeDuringNight = number.toString().toInt()
                }
            }
        })
        inflated.total_time_awake_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                    parentActivity.morningData!!.totalTimeAwakeDuringNight = 0
                } else {
                    parentActivity.morningData!!.totalTimeAwakeDuringNight = number.toString().toInt()
                }

            }
        })


        return inflated
    }
}