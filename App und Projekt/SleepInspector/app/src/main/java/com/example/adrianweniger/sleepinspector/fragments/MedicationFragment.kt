package com.example.adrianweniger.sleepinspector.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.MorningEditActivity
import kotlinx.android.synthetic.main.fragment_medication.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class MedicationFragment : Fragment() {

    val TAG = "MEDICATION FRAGMENT"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_medication, container, false)
        var parentActivity = activity as MorningEditActivity

        //listeners
        inflated.medication_input.onChangeListener = fun (itemList: MutableList<String>){
            parentActivity.morningData!!.medication = itemList
        }


        return inflated
    }


}