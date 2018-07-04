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
import com.example.adrianweniger.sleepinspector.activities.PersonalInfoEditActivity
import kotlinx.android.synthetic.main.fragment_sleep_disorder.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class SleepDisorderFragment: Fragment() {

    val TAG = "SLEEP DISORDER FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // load view
        var inflated = inflater!!.inflate(R.layout.fragment_sleep_disorder, container, false)
        var parentActivity = activity as PersonalInfoEditActivity

        // set default / precomputed data
        inflated.sleep_pattern_problems.isChecked = parentActivity.personalInfoData!!.sleepPattern != 0
        inflated.stay_awake_problem.isChecked = parentActivity.personalInfoData!!.stayAwake != 0
        inflated.frequency_problems.setText(parentActivity.personalInfoData!!.amountPerWeek.toString(), TextView.BufferType.EDITABLE)
        inflated.since_when_years.setText(parentActivity.personalInfoData!!.sinceWhenYears.toString(), TextView.BufferType.EDITABLE)
        inflated.since_when_months.setText(parentActivity.personalInfoData!!.sinceWhenMonths.toString(), TextView.BufferType.EDITABLE)
        inflated.other_sleep_disorders.setText(parentActivity.personalInfoData!!.otherSleepProblems, TextView.BufferType.EDITABLE)


        //listeners
        inflated.sleep_pattern_problems.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, isChecked.toString())
            parentActivity.personalInfoData!!.sleepPattern = if(isChecked) 1 else 0
            Log.d(TAG, parentActivity.personalInfoData!!.sleepPattern.toString())
        }

        inflated.stay_awake_problem.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, isChecked.toString())

            parentActivity.personalInfoData!!.stayAwake = if(isChecked) 1 else 0
            Log.d(TAG, parentActivity.personalInfoData!!.stayAwake.toString())
        }

        inflated.frequency_problems.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.amountPerWeek = 0
                } else {
                    parentActivity.personalInfoData!!.amountPerWeek = text.toString().toInt()
                }
            }
        })

        inflated.since_when_years.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.sinceWhenYears = 0
                } else {
                    parentActivity.personalInfoData!!.sinceWhenYears = text.toString().toInt()
                }
            }
        })

        inflated.other_sleep_disorders.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.otherSleepProblems = ""
                } else {
                    parentActivity.personalInfoData!!.otherSleepProblems = text.toString()
                }
            }
        })


    return inflated
    }
}