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
import kotlinx.android.synthetic.main.fragment_ris_nonprecomputable_values.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class RISNonprecomputableValuesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_ris_nonprecomputable_values, container, false)
        var parentActivity = activity as RISEditActivity


        // set default / pre-existing data
        inflated.noise_affinity_preview.text = ScaleWordings.likert[parentActivity.risData!!.noiseAffinity]
        inflated.noise_affinity.progress = parentActivity.risData!!.noiseAffinity

        inflated.felt_sleep_quality_preview.text = ScaleWordings.likert[parentActivity.risData!!.feltSleepQuality]
        inflated.felt_sleep_quality.progress = parentActivity.risData!!.feltSleepQuality

        inflated.sleep_pondering_preview.text = ScaleWordings.likert[parentActivity.risData!!.sleepPondering]
        inflated.sleep_pondering.progress = parentActivity.risData!!.sleepPondering

        inflated.sleep_fear_preview.text = ScaleWordings.likert[parentActivity.risData!!.sleepFear]
        inflated.sleep_fear.progress = parentActivity.risData!!.sleepFear

        inflated.early_sleep_end_preview.text = ScaleWordings.likert[parentActivity.risData!!.earlySleepEnd]
        inflated.early_sleep_end.progress = parentActivity.risData!!.earlySleepEnd



        // listeners
        inflated.noise_affinity.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.noiseAffinity = p1
                inflated.noise_affinity_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.felt_sleep_quality.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.feltSleepQuality = p1
                inflated.felt_sleep_quality_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.sleep_pondering.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.sleepPondering = p1
                inflated.sleep_pondering_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.sleep_fear.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.sleepFear = p1
                inflated.sleep_fear_preview.text = ScaleWordings.likert[p1]
            }
        })

        inflated.early_sleep_end.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                parentActivity.risData!!.earlySleepEnd = p1
                inflated.early_sleep_end_preview.text = ScaleWordings.likert[p1]
            }
        })


        return inflated

    }
}