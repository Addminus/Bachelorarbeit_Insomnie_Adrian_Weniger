package com.example.adrianweniger.sleepinspector.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.example.adrianweniger.sleepinspector.DataClasses.PersonalInfoData
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.database.database
import com.example.adrianweniger.sleepinspector.fragments.DiseasesFragment
import com.example.adrianweniger.sleepinspector.fragments.PersonInfoFragment
import com.example.adrianweniger.sleepinspector.fragments.SleepDisorderFragment


import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * used to handle creation or updating of the personal data entry
 * displays the fragments that display the form
 */
class PersonalInfoEditActivity : AppCompatActivity() {

    val TAG="EVENING EDIT ACTIVITY"
    //initialise empty/default data
    private var currentStep = 0
    var personalInfoData: PersonalInfoData? = PersonalInfoData("", "",0, 0, "", 0, 0, 0, "", "", "", 0, 0, 0, 0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        //get existing entry from DB for updating. handle case if no entry in DB
        val dateKey = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(System.currentTimeMillis()))

        //get existing entry from DB for updating. handle case if no entry in DB
        personalInfoData = database.getPersonalInfo()
        if(personalInfoData == null){
            personalInfoData = PersonalInfoData("", "",0, 0, "", 0, 0, 0, "", "", "", 0, 0, 0, 0, "")
            personalInfoData!!.date = dateKey
        }

        //set up navigation that leads through the form
        stepView.setStepsNumber(3)
        displayCurrentFragment(currentStep)
        back.visibility = View.GONE
        next.paintFlags = next.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        back.paintFlags = back.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //set up listeners
        next.setOnClickListener({
            when(currentStep){
                in 0..stepView.stepCount - 2 -> {
                    currentStep++
                    stepView.go(currentStep, true)
                    displayCurrentFragment(currentStep)
                    back.visibility = View.VISIBLE
                    if (currentStep == stepView.stepCount-1)  {
                        next.text = getText(R.string.save)
                        next.setTextColor(resources.getColor(R.color.green)) //TODO add sdk if

                    }else {
                        next.text = getText(R.string.next)
                        next.setTextColor(resources.getColor(R.color.colorPrimaryDark)) //TODO add sdk if
                    }
                }
                stepView.stepCount - 1 -> {
                    stepView.done(true)

                    database.upsertPersonalData(personalInfoData!!)
                    Log.d(TAG, personalInfoData.toString())

                    //intent back to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

        back.setOnClickListener({
            if (currentStep > 0) {
                currentStep--
            }
            if(currentStep == 0){
                back.visibility = View.GONE
            }
            stepView.done(false)
            stepView.go(currentStep, true)
            displayCurrentFragment(currentStep)
            next.text = getText(R.string.next)
            next.setTextColor(resources.getColor(R.color.colorPrimaryDark)) //TODO add sdk if
        })
    }

    //methods below load and display fragments
    private fun displayCurrentFragment(currentStep: Int){
        when(currentStep){
            0 -> showFragment(PersonInfoFragment())
            1 -> showFragment(SleepDisorderFragment())
            2 -> showFragment(DiseasesFragment())
        }
    }

    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_fragment_holder, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

}
