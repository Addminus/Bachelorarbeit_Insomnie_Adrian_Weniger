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
import com.example.adrianweniger.sleepinspector.activities.PersonalInfoEditActivity
import kotlinx.android.synthetic.main.fragment_diseases.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */

class DiseasesFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_diseases, container, false)
        var parentActivity = activity as PersonalInfoEditActivity

        // set pre-existing/default values
        inflated.other_diseases.setText(parentActivity.personalInfoData!!.otherDiseases, TextView.BufferType.EDITABLE)
        inflated.general_medication.setText(parentActivity.personalInfoData!!.generalMedication, TextView.BufferType.EDITABLE)

        //listeners
        inflated.other_diseases.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.otherDiseases = ""
                } else {
                    parentActivity.personalInfoData!!.otherDiseases = text.toString()
                }
            }
        })

        inflated.general_medication.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.generalMedication = ""
                } else {
                    parentActivity.personalInfoData!!.generalMedication = text.toString()
                }
            }
        })


        return inflated
    }
}