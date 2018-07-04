package com.example.adrianweniger.sleepinspector.DataClasses

/**
 * Kotlin Data Class representing a morning entry
 */
data class MorningData (
        var date:String,
        var dateInMillis:Long,
        var sleepQuality:Int,// 0-4
        var morningFeeling:Int, // 0-5
        var timeUntilSleepStart:Int,
        var timesAwakeDuringNight:Int,
        var totalTimeAwakeDuringNight:Int,
        var sleepEnd:String,
        var outOfBedTime: String,
        var medication:MutableList<String>,
        var sleepTime:Int,
        var sleepTimeString: String
)