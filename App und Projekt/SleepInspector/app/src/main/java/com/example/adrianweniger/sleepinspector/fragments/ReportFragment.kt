package com.example.adrianweniger.sleepinspector.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.os.Build
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import com.example.adrianweniger.sleepinspector.DataClasses.EveningData
import com.example.adrianweniger.sleepinspector.DataClasses.MorningData
import com.example.adrianweniger.sleepinspector.PDFCreator
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.database.database
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.fragment_report.view.*
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.charts.LineChart
import android.widget.TextView
import com.example.adrianweniger.sleepinspector.DataClasses.RISData
import com.example.adrianweniger.sleepinspector.XAxisValueFormatter
import com.example.adrianweniger.sleepinspector.ValueFormatter
import com.github.mikephil.charting.components.Description
import kotlinx.android.synthetic.main.fragment_sleep_start.*
import kotlin.collections.ArrayList


/**
 * Fragment showing Report View
 * shows general information about diseas state
 * start creation of pdf report
 */
class ReportFragment : Fragment() {

    val TAG="REPORT FRAGMENT"
    //init data
    private var eveningDataList = mutableListOf<EveningData>()
    private var morningDataList = mutableListOf<MorningData>()
    private var risDataList = mutableListOf<RISData>()
    private var initialX: Float = 0f

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_report, container, false)
        val parentActivity = activity
        PDFCreator().verifyStoragePermissions(parentActivity)
        // get Colors
        val primaryDarkColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorPrimaryDark)    } else {
            resources.getColor(R.color.colorPrimaryDark)
        }
        // set up view flipper
        inflated.chart_holder.isAutoStart = false
        inflated.chart_holder.setInAnimation(context, android.R.anim.fade_in)
        inflated.chart_holder.setInAnimation(context, android.R.anim.fade_out)

        // get data from DB
        eveningDataList = context.database.getAllEveningData()
        morningDataList = context.database.getAllMorningData()
        risDataList = context.database.getAllRISData()

        // set up lists to hold entries for linecharts
        val morningFeelingEntries = ArrayList<Entry>()
        val dailyPerformanceEntries = ArrayList<Entry>()
        val dailyFatigueEntries = ArrayList<Entry>()
        val eveningFeelingEntries = ArrayList<Entry>()
        val sleepQualityEntries = ArrayList<Entry>()
        val sleepTimeEntries = ArrayList<Entry> ()

        // create linechart entries
        for ( data in morningDataList){
            morningFeelingEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.morningFeeling.toFloat()))
        }
        for ( data in eveningDataList){
            dailyPerformanceEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.dailyPerformance.toFloat()))
        }
        for ( data in eveningDataList){
            dailyFatigueEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.dailyFatigue.toFloat()))
        }
        for ( data in eveningDataList){
            eveningFeelingEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.eveningFeeling.toFloat()))
        }
        for ( data in morningDataList){
            sleepQualityEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.sleepQuality.toFloat()))
        }
        for ( data in morningDataList){
            sleepTimeEntries.add(Entry((data.dateInMillis /1000/60/60/24 +1).toFloat(), data.sleepTime.toFloat()))
        }

            // add linecharts to viewflipper

            if(morningDataList.isNotEmpty() && eveningDataList.isNotEmpty()){
                inflated.chart_holder.addView(fillChart(morningFeelingEntries,"Gefühl am Morgen", context))
                inflated.chart_holder.addView(fillChart(sleepQualityEntries,"Schlaf Qualität", context))
                inflated.chart_holder.addView(fillChart(sleepTimeEntries,"Schlafdauer", context))
                inflated.chart_holder.addView(fillChart(dailyPerformanceEntries, "Tägliche Leistungsfähigkeit", context))
                inflated.chart_holder.addView(fillChart(dailyFatigueEntries,"Müdigkeit am Tag", context))
                inflated.chart_holder.addView(fillChart(eveningFeelingEntries,"Gefühl am Abend", context))
            } else if(morningDataList.isNotEmpty()){
                inflated.chart_holder.addView(fillChart(morningFeelingEntries,"Gefühl am Morgen", context))
                inflated.chart_holder.addView(fillChart(sleepQualityEntries,"Schlaf Qualität", context))
                inflated.chart_holder.addView(fillChart(sleepTimeEntries,"Schlafdauer", context))
            } else if (eveningDataList.isNotEmpty()){
                inflated.chart_holder.addView(fillChart(dailyPerformanceEntries, "Tägliche Leistungsfähigkeit", context))
                inflated.chart_holder.addView(fillChart(dailyFatigueEntries,"Müdigkeit am Tag", context))
                inflated.chart_holder.addView(fillChart(eveningFeelingEntries,"Gefühl am Abend", context))
            } else {
                val tv = TextView(context)
                tv.text = "Keine Daten vorhanden. Füllen Sie das Schlaftagebuch aus"
                tv.setTextColor(primaryDarkColor)
                tv.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                tv.height = 500
                inflated.chart_holder.addView(tv)
            }

        //show last RIS result
        if (risDataList.isNotEmpty()) {
            val risData = risDataList.last()
            Log.d(TAG, risData.toString())
            inflated.last_ris.text = risData.scoreText
            inflated.disclaimer.visibility = View.VISIBLE
        } else {
            inflated.disclaimer.visibility = View.GONE
            inflated.last_ris.text = "Keinen RIS-Eintrag ausgefüllt. \n Fülle einen Eintrag im Kalender aus."
        }

        //listeners
        inflated.report_fragment.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    initialX = motionEvent.x
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    val finalX = motionEvent.x
                    if(initialX > finalX){
                        inflated.chart_holder.setInAnimation(context, R.anim.in_right)
                        inflated.chart_holder.setOutAnimation(context, R.anim.out_left)
                        inflated.chart_holder.showNext()
                        return@setOnTouchListener true

                    } else {
                        inflated.chart_holder.setInAnimation(context, R.anim.in_left)
                        inflated.chart_holder.setOutAnimation(context, R.anim.out_right)
                        inflated.chart_holder.showPrevious()
                        return@setOnTouchListener true
                    }
                }
                else -> return@setOnTouchListener false
            }
        }

        inflated.create_pdf_btn.paintFlags = inflated.create_pdf_btn.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //listener to create PDF Report
        inflated.create_pdf_btn.setOnClickListener({
            if(PDFCreator().verifyStoragePermissions(parentActivity)){
                PDFCreator().createPDFReport(parentActivity.applicationContext)
            }

        })
        return inflated
    }




    private fun fillChart(entries: ArrayList<Entry>, label: String, ctx: Context): LineChart{

        val primaryDarkColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorPrimaryDark)    } else {
            resources.getColor(R.color.colorPrimaryDark)
        }

        val chart = LineChart(ctx)
        val description = Description()
        description.text = ""
        chart.description = description
        chart.layoutParams = ViewGroup.LayoutParams(Resources.getSystem().displayMetrics.widthPixels, 500)

        val dataSet = LineDataSet(entries as MutableList<Entry>?, label)
        dataSet.color = R.color.colorPrimary
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.1f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = primaryDarkColor
        dataSet.fillAlpha = 255

        val lineData = LineData(dataSet)
        lineData.setValueFormatter(ValueFormatter(label))

        if(entries.isNotEmpty()) {
            val leftAxis = chart.axisLeft
            leftAxis.axisMinimum = 0f
            leftAxis.granularity = 1f
            leftAxis.setDrawGridLines(false)
            leftAxis.setDrawAxisLine(true)
            leftAxis.setDrawLabels(false)

            val rightAxis = chart.axisRight
            rightAxis.axisMinimum = 0f
            rightAxis.granularity = 1f
            rightAxis.setDrawGridLines(false)
            rightAxis.setDrawAxisLine(false)
            rightAxis.setDrawLabels(false)

            val xAxis = chart.xAxis
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.granularity = (86400000 / 1000 / 60 / 60 / 24).toFloat()
            chart.data = lineData
            xAxis.axisMinimum = entries[0].x
            xAxis.labelCount = 3
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(true)
            xAxis.valueFormatter = XAxisValueFormatter(entries)
            chart.invalidate()
        }

        return chart
    }
}