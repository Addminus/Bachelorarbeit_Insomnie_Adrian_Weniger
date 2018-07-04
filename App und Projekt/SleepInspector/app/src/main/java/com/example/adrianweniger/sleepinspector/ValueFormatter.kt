package com.example.adrianweniger.sleepinspector

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Formats Linechart Y Axis values
 */
class ValueFormatter(label: String) : IValueFormatter {

    private var label = label


    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        return when(label){
            "Gefühl am Morgen" -> ScaleWordings.morningFeelingParams[value.toLong().toInt()]
            "Tägliche Leistungsfähigkeit" -> ScaleWordings.dailyPerformanceParams[value.toLong().toInt()]
            "Müdigkeit am Tag" -> ScaleWordings.dailyFatigueParams[value.toLong().toInt()]
            "Gefühl am Abend" -> ScaleWordings.eveningFeelingParams[value.toLong().toInt()]
            "Schlafqualität" -> ScaleWordings.sleepQualityParams[value.toLong().toInt()]
            "Schlafdauer" -> "${value/60} Std."
            else -> ""
        }
    }
}