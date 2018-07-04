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
import kotlinx.android.synthetic.main.fragment_daily_performance.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

class DailyPerformanceFragment : Fragment() {

    val TAG = "DAILY FRAGMENT FRAGMENT"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_daily_performance, container, false)

        var parentActivity = activity as EveningEditActivity

        // get Colors || check for android versions as some calls are deprecated
        val redcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

        // set default / pre-existing data
        inflated.daily_performance_params.max = ScaleWordings.dailyPerformanceParams.size - 1
        inflated.daily_fatigue_params.max = ScaleWordings.dailyFatigueParams.size - 1

        inflated.daily_performance_params_preview.text = ScaleWordings.dailyPerformanceParams[parentActivity.eveningData!!.dailyPerformance]
        inflated.daily_performance_params.progress = parentActivity.eveningData!!.dailyPerformance

        inflated.daily_fatigue_params_preview.text = ScaleWordings.dailyFatigueParams[parentActivity.eveningData!!.dailyFatigue]
        inflated.daily_fatigue_params.progress = parentActivity.eveningData!!.dailyFatigue



        // set the text colors for the seekbar values
        when {
            inflated.daily_performance_params.progress < 2 -> inflated.daily_performance_params_preview.setTextColor(redcolor)
            inflated.daily_performance_params.progress in 2..3 -> inflated.daily_performance_params_preview.setTextColor(orangeColor)
            else -> inflated.daily_performance_params_preview.setTextColor(greenColor)
        }

        when (inflated.daily_fatigue_params.progress) {
            1 -> inflated.daily_fatigue_params_preview.setTextColor(redcolor)
            in 2..3 -> inflated.daily_fatigue_params_preview.setTextColor(orangeColor)
            else -> inflated.daily_fatigue_params_preview.setTextColor(greenColor)
        }


        //listeners
        inflated.daily_performance_params.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.eveningData!!.dailyPerformance = p1
                inflated.daily_performance_params_preview.text = ScaleWordings.dailyPerformanceParams[p1]
                when {
                    p1 < 2 -> inflated.daily_performance_params_preview.setTextColor(redcolor)
                    p1 in 2..3 -> inflated.daily_performance_params_preview.setTextColor(orangeColor)
                    else -> inflated.daily_performance_params_preview.setTextColor(greenColor)
                }
            }
        })

        inflated.daily_fatigue_params.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.eveningData!!.dailyFatigue = p1
                inflated.daily_fatigue_params_preview.text = ScaleWordings.dailyFatigueParams[p1]
                when (p1) {
                    1 -> inflated.daily_fatigue_params_preview.setTextColor(redcolor)
                    in 2..3 -> inflated.daily_fatigue_params_preview.setTextColor(orangeColor)
                    else -> inflated.daily_fatigue_params_preview.setTextColor(greenColor)
                }
            }
        })


        return inflated
    }
}