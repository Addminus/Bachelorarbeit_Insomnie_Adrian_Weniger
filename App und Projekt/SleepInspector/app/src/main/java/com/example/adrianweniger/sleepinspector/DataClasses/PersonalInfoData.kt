package com.example.adrianweniger.sleepinspector.DataClasses

/**
 * Kotlin Data Class representing a personal entry
 */
data class PersonalInfoData(
        var name: String,
        var birthday: String,
        var sleepPattern:Int,
        var stayAwake: Int,
        var otherSleepProblems:String,
        var amountPerWeek: Int,
        var sinceWhenYears: Int,
        var sinceWhenMonths: Int,
        var otherDiseases: String,
        var generalMedication: String,
        var profession: String,
        var height: Int,
        var weight: Int,
        var numberHouseholdMembers: Int,
        var numberChildren: Int,
        var date: String
)