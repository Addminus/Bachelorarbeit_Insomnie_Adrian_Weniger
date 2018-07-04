package com.example.adrianweniger.sleepinspector.DataClasses

/**
 * Kotlin Data Class representing a RIS entry
 */
data class RISData (
    var date: String,
    var dateInMillis: Long,

    var avgSleepStart: String,
    var avgSleepEnd: String,

    var avgTimeUntilAsleep: Int,
    var avgSleepTime: Int,
    var sleepInterruption: Int,
    var earlySleepEnd: Int,
    var noiseAffinity: Int,
    var feltSleepQuality: Int,
    var sleepPondering: Int,
    var sleepFear: Int,
    var feltPerformance: Int,
    var medication: Int,
    var score: Int,
    var scoreText:String
)