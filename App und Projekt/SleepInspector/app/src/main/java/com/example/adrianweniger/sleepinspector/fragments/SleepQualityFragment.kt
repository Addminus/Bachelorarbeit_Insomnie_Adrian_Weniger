package com.example.adrianweniger.sleepinspector.fragments

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.adrianweniger.sleepinspector.ScaleWordings
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import kotlinx.android.synthetic.main.fragment_sleep_quality.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

public class SleepQualityFragment : Fragment() {

    val TAG="SLEEP QUALITY FRAGMENT"

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //inflate the layout for this fragment
        var inflated = inflater.inflate(R.layout.fragment_sleep_quality, container, false)
         // get colors
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
         val parentActivity = activity as MorningEditActivity

         // set up views
         inflated.sleep_quality_params.max = ScaleWordings.sleepQualityParams.size - 1
         inflated.morning_feeling_params.max = ScaleWordings.morningFeelingParams.size - 1

         inflated.sleep_quality_params_preview.text = ScaleWordings.sleepQualityParams[parentActivity.morningData!!.sleepQuality]
         inflated.sleep_quality_params.progress = parentActivity.morningData!!.sleepQuality

         inflated.morning_feeling_param_preview.text = ScaleWordings.morningFeelingParams[parentActivity.morningData!!.morningFeeling]
         inflated.morning_feeling_params.progress = parentActivity.morningData!!.morningFeeling

         when {
             inflated.sleep_quality_params.progress < 2 -> inflated.sleep_quality_params_preview.setTextColor(redColor)
             inflated.sleep_quality_params.progress == 2 -> inflated.sleep_quality_params_preview.setTextColor(orangeColor)
             else -> inflated.sleep_quality_params_preview.setTextColor(greenColor)
         }
         when {
             inflated.morning_feeling_params.progress < 2 -> inflated.morning_feeling_param_preview.setTextColor(redColor)
             inflated.morning_feeling_params.progress in 2..3 -> inflated.morning_feeling_param_preview.setTextColor(orangeColor)
             else -> inflated.morning_feeling_param_preview.setTextColor(greenColor)
         }


         // listeners
         inflated.sleep_quality_params.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
             override fun onStopTrackingTouch(p0: SeekBar?) {
             }

             override fun onStartTrackingTouch(p0: SeekBar?) {
             }

             override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                 parentActivity.morningData!!.sleepQuality = p1
                 inflated.sleep_quality_params_preview.text = ScaleWordings.sleepQualityParams[p1]
                 when {
                     p1 < 2 -> inflated.sleep_quality_params_preview.setTextColor(redColor)
                     p1 == 2 -> inflated.sleep_quality_params_preview.setTextColor(orangeColor)
                     else -> inflated.sleep_quality_params_preview.setTextColor(greenColor)
                 }
             }
         })

         inflated.morning_feeling_params.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
             override fun onStopTrackingTouch(p0: SeekBar?) {
             }

             override fun onStartTrackingTouch(p0: SeekBar?) {
             }

             override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                 parentActivity.morningData!!.morningFeeling = p1
                 inflated.morning_feeling_param_preview.text = ScaleWordings.morningFeelingParams[p1]
                 when {
                     p1 < 2 -> inflated.morning_feeling_param_preview.setTextColor(redColor)
                     p1 in 2..3 -> inflated.morning_feeling_param_preview.setTextColor(orangeColor)
                     else -> inflated.morning_feeling_param_preview.setTextColor(greenColor)
                 }
             }
         })


         return inflated

    }


}