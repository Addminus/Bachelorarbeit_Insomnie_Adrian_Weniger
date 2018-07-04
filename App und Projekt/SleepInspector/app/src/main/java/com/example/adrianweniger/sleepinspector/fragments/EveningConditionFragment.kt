package com.example.adrianweniger.sleepinspector.fragments

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.ScaleWordings
import com.example.adrianweniger.sleepinspector.activities.EveningEditActivity
import kotlinx.android.synthetic.main.fragment_evening_condition.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

class EveningConditionFragment : Fragment() {

    val TAG = "EVENING CONDITION FRAGMENT"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // load view
        var inflated = inflater!!.inflate(R.layout.fragment_evening_condition, container, false)
        var parentActivity = activity as EveningEditActivity

        // get Colors
        val redColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.red)    } else {
            resources.getColor(R.color.red)
        }
        val greenColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.green)    } else {
            resources.getColor(R.color.green)
        }
        val orangeColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.orange)    } else {
            resources.getColor(R.color.orange)
        }

        // set up views and set un default / pre-existing values
        inflated.evening_feeling_params.max = ScaleWordings.eveningFeelingParams.size -1
        inflated.evening_feeling_params_preview.text = ScaleWordings.eveningFeelingParams[parentActivity.eveningData!!.eveningFeeling]
        inflated.evening_feeling_params.progress = parentActivity.eveningData!!.eveningFeeling
        when {
            inflated.evening_feeling_params.progress < 2 -> inflated.evening_feeling_params_preview.setTextColor(redColor)
            inflated.evening_feeling_params.progress in 2..3 -> inflated.evening_feeling_params_preview.setTextColor(orangeColor)
            else -> inflated.evening_feeling_params_preview.setTextColor(greenColor)
        }


        //listeners
        inflated.evening_feeling_params.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.eveningData!!.eveningFeeling = p1
                inflated.evening_feeling_params_preview.text = ScaleWordings.eveningFeelingParams[p1]
                when {
                    p1 < 2 -> inflated.evening_feeling_params_preview.setTextColor(redColor)
                    p1 in 2..3 -> inflated.evening_feeling_params_preview.setTextColor(orangeColor)
                    else -> inflated.evening_feeling_params_preview.setTextColor(greenColor)
                }
            }
        })



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            inflated.bedtime_picker.hour = 8
            inflated.bedtime_picker.minute = 0
        } else {
            inflated.bedtime_picker.currentHour = 9
            inflated.bedtime_picker.currentMinute = 0
        }
        inflated.bedtime_picker.setIs24HourView(true)

        inflated.bedtime_picker.setOnTimeChangedListener({ picker, hour, min ->
            parentActivity.eveningData!!.bedTime = hour.toString() + ":" + min.toString()
        })
        return inflated
    }
}