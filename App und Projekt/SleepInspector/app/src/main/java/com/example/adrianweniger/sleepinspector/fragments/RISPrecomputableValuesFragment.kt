package com.example.adrianweniger.sleepinspector.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.ScaleWordings
import com.example.adrianweniger.sleepinspector.activities.RISEditActivity
import kotlinx.android.synthetic.main.fragment_ris_precomputable_values_display.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

class RISPrecomputableValuesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var inflated = inflater!!.inflate(R.layout.fragment_ris_precomputable_values_display, container, false)
        var parentActivity = activity as RISEditActivity

        // set default / precomputed / pre-existing data
        inflated.avg_sleep_time_preview.text = ScaleWordings.likert[parentActivity.risData!!.avgSleepTime]
        inflated.avg_sleep_time.progress = parentActivity.risData!!.avgSleepTime

        inflated.avg_time_until_asleep_preview.text = ScaleWordings.likert[parentActivity.risData!!.avgTimeUntilAsleep]
        inflated.avg_time_until_asleep.progress = parentActivity.risData!!.avgTimeUntilAsleep

        inflated.sleep_interruption_preview.text = ScaleWordings.likert[parentActivity.risData!!.sleepInterruption]
        inflated.sleep_interruption.progress = parentActivity.risData!!.sleepInterruption

        inflated.felt_performance_preview.text = ScaleWordings.likert[parentActivity.risData!!.feltPerformance]
        inflated.felt_performance.progress = parentActivity.risData!!.feltPerformance

        inflated.ris_medication_preview.text = ScaleWordings.likert[parentActivity.risData!!.medication]
        inflated.ris_medication.progress = parentActivity.risData!!.medication

        // convert entry number data to wording
        when(inflated.avg_sleep_time.progress){
            0 -> inflated.avg_sleep_time_preview.text = "7 und mehr"
            1 -> inflated.avg_sleep_time_preview.text = "5-6"
            2 -> inflated.avg_sleep_time_preview.text = "4"
            3 -> inflated.avg_sleep_time_preview.text = "2-3"
            4 -> inflated.avg_sleep_time_preview.text = "0-1"
        }
        when(inflated.avg_time_until_asleep.progress){
            0 -> inflated.avg_time_until_asleep_preview.text = "1-20 min"
            1 -> inflated.avg_time_until_asleep_preview.text = "21-40 min"
            2 -> inflated.avg_time_until_asleep_preview.text = "41-60 min"
            3 -> inflated.avg_time_until_asleep_preview.text = "61-90 min"
            4 -> inflated.avg_time_until_asleep_preview.text = "mehr als 90 min"
        }


        // listeners
        inflated.avg_time_until_asleep.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.avgTimeUntilAsleep = p1
                when(p1){
                    0 -> inflated.avg_time_until_asleep_preview.text = "1-20 min"
                    1 -> inflated.avg_time_until_asleep_preview.text = "21-40 min"
                    2 -> inflated.avg_time_until_asleep_preview.text = "41-60 min"
                    3 -> inflated.avg_time_until_asleep_preview.text = "61-90 min"
                    4 -> inflated.avg_time_until_asleep_preview.text = "mehr als 90 min"
                }
            }
        })

        inflated.avg_sleep_time.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.avgSleepTime = p1
                when(p1){
                    0 -> inflated.avg_sleep_time_preview.text = "7 und mehr"
                    1 -> inflated.avg_sleep_time_preview.text = "5-6"
                    2 -> inflated.avg_sleep_time_preview.text = "4"
                    3 -> inflated.avg_sleep_time_preview.text = "2-3"
                    4 -> inflated.avg_sleep_time_preview.text = "0-1"

                }
            }
        })

        inflated.sleep_interruption.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.sleepInterruption = p1
                inflated.sleep_interruption_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.felt_performance.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.feltPerformance = p1
                inflated.felt_performance_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.ris_medication.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.medication = p1
                inflated.ris_medication_preview.text = ScaleWordings.likert[p1]
            }
        })

        return inflated

    }
}