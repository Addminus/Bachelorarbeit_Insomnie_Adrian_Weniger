package com.example.adrianweniger.sleepinspector.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.EveningEditActivity
import kotlinx.android.synthetic.main.fragment_daily_alcohol.view.*


/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

class DailyAlcoholFragment : Fragment() {

    val TAG = "DAILY ALC. FRAGMENT"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_daily_alcohol, container, false)
        var parentActivity = activity as EveningEditActivity


        inflated.daily_alcohol_input.onChangeListener = fun (alcohol: MutableList<String>){
            parentActivity.eveningData!!.dailyAlcohol = alcohol
        }


        return inflated
    }


}