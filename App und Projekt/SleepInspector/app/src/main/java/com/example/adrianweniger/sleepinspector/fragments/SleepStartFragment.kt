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


/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class SleepStartFragment : Fragment(){

    val TAG="SLEEP START FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_sleep_start, container, false)
        var parentActivity = activity as MorningEditActivity

        // set up view
        inflated.sleep_start_input.setText(parentActivity.morningData!!.timeUntilSleepStart.toString(), TextView.BufferType.EDITABLE)

        //listeners
        inflated.sleep_start_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(number: CharSequence?, start: Int, count: Int, after: Int) {
                if(number.isNullOrEmpty()){
                     parentActivity.morningData!!.timeUntilSleepStart = -1
                } else {
                    parentActivity.morningData!!.timeUntilSleepStart = number.toString().toInt()
                }
            }
        })
        return inflated
    }
}