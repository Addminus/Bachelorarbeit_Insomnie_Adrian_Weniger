package com.example.adrianweniger.sleepinspector.DataClasses

/**
 * Kotlin Data Class representing an evening entry
 */
data class EveningData (
        var date :String,
        var dateInMillis :Long,
        var dailyPerformance :Int, //0-5
        var dailyFatigue :Int, // 0-3
        var dailySleepAmount :Int,
        var dailySleepTime :Int,
        var dailyAlcohol : MutableList<String>,
        var bedTime :String,
        var eveningFeeling :Int // 0-5
)