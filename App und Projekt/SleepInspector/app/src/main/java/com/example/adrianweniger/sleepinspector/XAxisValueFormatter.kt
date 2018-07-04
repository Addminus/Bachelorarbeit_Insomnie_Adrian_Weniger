package com.example.adrianweniger.sleepinspector


import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList





/**
 * Formats Linechart X axis values (date is always displayed on x axis)
 */
class XAxisValueFormatter(entries: ArrayList<Entry>) : IAxisValueFormatter {

    private var labels = ArrayList<String>()

    init {
        for (entry in entries) {
            labels.add(SimpleDateFormat("dd/MM/yyyy").format(Date(entry.x.toLong())))
        }
    }


    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val time = value *1000*60*60*24
        val dateString = SimpleDateFormat("dd/MM/yyyy").format(Date(time.toLong()))
        return dateString
    }
}






