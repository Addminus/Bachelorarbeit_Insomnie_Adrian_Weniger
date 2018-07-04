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
import com.example.adrianweniger.sleepinspector.DataClasses.EveningData
import com.example.adrianweniger.sleepinspector.DataClasses.MorningData
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.database.database
import com.example.adrianweniger.sleepinspector.fragments.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * used to handle creation or updating of morning entries
 * displays the fragments that display the form
 * takes a timestamp via intent to associate entry with correct day
 */

class MorningEditActivity : AppCompatActivity(){

    val TAG="MORNING EDIT ACTIVITY"
    //initialise empty/default data
    private var currentStep = 0
    var morningData: MorningData? = MorningData("", 0,0,0,0,0,0,"---","---", mutableListOf(), 0, "---")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val inflater = LayoutInflater.from(this)
        val actionBar = inflater.inflate(R.layout.action_bar, null)
        val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        supportActionBar!!.setCustomView(actionBar, params)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        var dateInMillis = intent.extras["date"].toString().toLong()
        val dateKey = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(dateInMillis))

        //get existing entry from DB for updating. handle case if no entry in DB
        morningData = database.getMorningByDate(dateKey)
        if(morningData == null){
            morningData = MorningData("", 0,0,0,0,0,0,"---","---", mutableListOf(), 0, "---")
            morningData!!.date = dateKey
            morningData!!.dateInMillis = dateInMillis
        }

        //set up navigation that leads through the form
        stepView.setStepsNumber(6)
        displayCurrentFragment(currentStep)
        back.visibility = View.GONE
        next.paintFlags = next.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        back.paintFlags = back.paintFlags or Paint.UNDERLINE_TEXT_FLAG


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
                    database.upsertMorningData(morningData!!)
                    Log.d(TAG, morningData.toString())

                    //intent back to MainAtivity
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
            0 -> showFragment(SleepQualityFragment())
            1 -> showFragment(SleepStartFragment())
            2 -> showFragment(TimesAwakeDuringNightFragment())
            3 -> showFragment(SleepEndFragment())
            4 -> showFragment(MedicationFragment())
            5 -> showFragment(SleepTimeFragment())
        }
    }

    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_fragment_holder, fragment)
        transaction.commit()
    }
}