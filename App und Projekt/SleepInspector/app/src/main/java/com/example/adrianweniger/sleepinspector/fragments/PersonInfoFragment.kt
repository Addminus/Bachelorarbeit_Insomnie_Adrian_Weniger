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
import kotlinx.android.synthetic.main.fragment_person_info.view.*
import kotlinx.android.synthetic.main.fragment_sleep_start.view.*

/**
 * Fragment showing form part
 * handles form input, gives input data back to entry initialized in parentActivity
 */
class PersonInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_person_info, container, false)
        var parentActivity = activity as PersonalInfoEditActivity

        inflated.name.setText(parentActivity.personalInfoData!!.name, TextView.BufferType.EDITABLE)
        inflated.birthday.setText(parentActivity.personalInfoData!!.birthday, TextView.BufferType.EDITABLE)

        //listeners
        inflated.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.name = ""
                } else {
                    parentActivity.personalInfoData!!.name = text.toString()
                }
            }
        })
        inflated.birthday.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.birthday = ""
                } else {
                    parentActivity.personalInfoData!!.birthday = text.toString()
                }
            }
        })
        inflated.weight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.weight = 0
                } else {
                    parentActivity.personalInfoData!!.weight = text.toString().toInt()
                }
            }
        })
        inflated.height_info.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.height = 0
                } else {
                    parentActivity.personalInfoData!!.height = text.toString().toInt()
                }
            }
        })
        inflated.householdMembers.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.numberHouseholdMembers = 0
                } else {
                    parentActivity.personalInfoData!!.numberHouseholdMembers = text.toString().toInt()
                }
            }
        })
        inflated.children.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                if(text.isNullOrEmpty()){
                    parentActivity.personalInfoData!!.numberChildren = 0
                } else {
                    parentActivity.personalInfoData!!.numberChildren = text.toString().toInt()
                }
            }
        })


        return inflated
    }
}