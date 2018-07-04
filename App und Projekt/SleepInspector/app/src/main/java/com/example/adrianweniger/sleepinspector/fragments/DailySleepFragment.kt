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
import com.example.adrianweniger.sleepinspector.activities.EveningEditActivity
import kotlinx.android.synthetic.main.fragment_daily_sleep.view.*
import kotlinx.android.synthetic.main.fragment_sleep_start.view.*
import kotlinx.android.synthetic.main.fragment_times_awake_during_night.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class DailySleepFragment : Fragment() {

    val TAG = "DAILY SLEEP FRAGMENT"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_daily_sleep, container, false)
        var parentActivity = activity as EveningEditActivity

        //set default / pre-existing data
        inflated.total_time_daily_sleep.setText(parentActivity.eveningData!!.dailySleepAmount.toString(), TextView.BufferType.EDITABLE)


        //listeners
        inflated.total_time_daily_sleep.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                    parentActivity.eveningData!!.dailySleepAmount = -1
                } else {
                    parentActivity.eveningData!!.dailySleepAmount = number.toString().toInt()
                }
            }
        })
        return inflated
    }
}