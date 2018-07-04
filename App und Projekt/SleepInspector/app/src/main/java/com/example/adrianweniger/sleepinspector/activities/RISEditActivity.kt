package com.example.adrianweniger.sleepinspector.activities

import android.content.Intent
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.example.adrianweniger.sleepinspector.DataClasses.EveningData
import com.example.adrianweniger.sleepinspector.DataClasses.MorningData
import com.example.adrianweniger.sleepinspector.DataClasses.RISData
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.database.database
import com.example.adrianweniger.sleepinspector.fragments.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * used to handle creation or updating of RIS entries
 * displays the fragments that display the form
 * takes a timestamp via intent to associate entry with correct day
 */

class RISEditActivity : AppCompatActivity() {
    val TAG="RIS EDIT ACTIVITY"
    //initialise empty/default data
    private var currentStep = 0
    var risData:RISData? = RISData("", 0,"---", "---", 0,0,0,0,0,0,0,0,0, 0, 0, "---")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val inflater = LayoutInflater.from(this)
        val actionBar = inflater.inflate(R.layout.action_bar, null)
        val params = ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        supportActionBar!!.setCustomView(actionBar, params)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        var dateInMillis = intent.extras["date"].toString().toLong()
        val dateKey = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(dateInMillis))

        //get existing entry from DB for updating. handle case if no entry in DB
        risData = database.getRISByDate(dateKey)
        if(risData == null){
            risData = generatePrecomputedRIS(RISData("", 0,"---", "---", 0,0,0,0,0,0,0,0,0, 0, 0, "---"))
            risData!!.date = dateKey
            risData!!.dateInMillis = dateInMillis
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
                    if (currentStep == stepView.stepCount -1)  {
                        next.text = getText(R.string.save)
                        next.setTextColor(resources.getColor(R.color.green)) //TODO add sdk if
                    }else {
                        next.text = getText(R.string.next)
                        next.setTextColor(resources.getColor(R.color.colorPrimaryDark)) //TODO add sdk if
                    }
                }
                stepView.stepCount - 1 -> {
                    stepView.done(true)

                    //calculate and set RIS score
                    var score = risData!!.avgTimeUntilAsleep + risData!!.avgSleepTime + risData!!.sleepInterruption + risData!!.earlySleepEnd + risData!!.noiseAffinity + risData!!.noiseAffinity + risData!!.sleepPondering + risData!!.sleepFear + risData!!.feltPerformance + risData!!.medication
                    var scoreText = when (score) {
                        in 0..12 -> "unauffällig"
                        in 13..24 -> "auffällig"
                        in 25..40 -> "ausgeprägt"
                        else -> ""
                    }
                    risData!!.score = score
                    risData!!.scoreText = scoreText

                    database.upsertRISData(risData!!)
                    Log.d(TAG, risData.toString())

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
            0 -> showFragment(AvgSleepBordersFragment())
            1 -> showFragment(RISPrecomputableValuesFragment())
            2 -> showFragment(RISNonprecomputableValuesFragment())
        }
    }

    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_fragment_holder, fragment)
        transaction.commit()
    }

    //methods below generate precomputed values for RIS form
    private fun generatePrecomputedRIS(risData: RISData):RISData{
        var eveningDataList = database.getPreviousFourWeeksEvenings()
        var morningDataList = database.getPreviousFourWeeksMornings()

        if(morningDataList.isNotEmpty()){
            risData.medication = calculateMedication(morningDataList)
            risData.sleepInterruption = calculateAVGSleepInterruption(morningDataList)
            risData.avgTimeUntilAsleep = calculateAvgTimeUntil(morningDataList)
            risData.avgSleepTime = calculateAvgSleepTime(morningDataList)
        } else {
            risData.medication = 0
            risData.sleepInterruption = 0
            risData.avgTimeUntilAsleep = 0
            risData.avgSleepTime = 0
        }

        if(eveningDataList.isNotEmpty()){
            risData.feltPerformance = calculateFeltPerformance(eveningDataList)
        } else {
            risData.feltPerformance = 0
        }
        return risData
    }

    private fun calculateAvgTimeUntil(morningDataList:MutableList<MorningData>):Int{
        var totalTimeUntilSleepStart = 0
        for (morning in morningDataList){
            totalTimeUntilSleepStart += morning.timeUntilSleepStart
        }

        return when (totalTimeUntilSleepStart / morningDataList.count()){
            in Float.NEGATIVE_INFINITY.toInt()..20 -> 0
            in 21..40 -> 1
            in 41..60 -> 2
            in 61..90 -> 3
            in 90..Float.POSITIVE_INFINITY.toInt() -> 4
            else -> 0
        }
    }

    private fun calculateAvgSleepTime (morningDataList:MutableList<MorningData>): Int{
        var totalSleepTime = 0
        for (morning in morningDataList){
            totalSleepTime += morning.sleepTime
        }
        return when (totalSleepTime / 60 / morningDataList.count()){
            in 7..100 -> 0
            in 5..6 -> 1
            4 -> 2
            in 2..3 -> 3
            in 0..1 -> 4
            else -> 0
        }
    }

    private fun calculateFeltPerformance(eveningDataList:MutableList<EveningData>): Int {
        var totalPerformance = 0.0
        for (evening in eveningDataList) {
            totalPerformance += evening.dailyPerformance
        }

        var avg = Math.floor(totalPerformance / eveningDataList.count()).toInt()

        return when (avg) {
            0 -> 0
            1 -> 1
            in 3..3 -> 2
            4 -> 3
            5 -> 4
            else -> 0
        }
    }

    private fun calculateMedication(morningDataList:MutableList<MorningData>): Int {
        var timesMedicationUsed = 0
        for (morning in morningDataList){
            if(morning.medication.isNotEmpty()){
                timesMedicationUsed++
            }
        }
        var avg = (timesMedicationUsed / morningDataList.count())*100
        return when (avg){
            0 -> 0
            in 1..30 -> 1
            in 31..60->2
            in 61..99 -> 3
            100 -> 4
            else -> 0

        }
    }

    private fun calculateAVGSleepInterruption(morningDataList:MutableList<MorningData>):Int {
        var timesWokenUp = 0
        for (morning in morningDataList){
            if(morning.timesAwakeDuringNight != 0){
                timesWokenUp++
            }
        }
        var avg = (timesWokenUp / morningDataList.count())*100
        return when (avg){
            0 -> 0
            in 1..30 -> 1
            in 31..60->2
            in 61..99 -> 3
            100 -> 4
            else -> 0

        }
    }



}
