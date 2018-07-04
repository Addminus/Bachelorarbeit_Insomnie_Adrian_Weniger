package com.example.adrianweniger.sleepinspector.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.adrianweniger.sleepinspector.DataClasses.EveningData
import com.example.adrianweniger.sleepinspector.DataClasses.MorningData
import com.example.adrianweniger.sleepinspector.DataClasses.PersonalInfoData
import com.example.adrianweniger.sleepinspector.DataClasses.RISData
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Connection to Database
 * implements logic to save, get and update data in DB
 */
class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {

    val TAG="MYSQL HELPER"

    companion object {
        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    //create DB tables
    override fun onCreate(db: SQLiteDatabase) {

        db.createTable("PersonalInfo", true,
                "Name" to TEXT + PRIMARY_KEY,
                "birthday" to TEXT,
                "sayAsleep" to INTEGER,
                "stayAwake" to INTEGER,
                "otherSleepProblems" to TEXT,
                "amountPerWeek" to INTEGER,
                "sinceWhenYears" to INTEGER,
                "sinceWhenMonths" to INTEGER,
                "otherDiseases" to TEXT,
                "generalMedication" to TEXT,
                "profession" to INTEGER,
                "height" to INTEGER,
                "weight" to INTEGER,
                "numberHouseholdmembers" to INTEGER,
                "numberChildren" to INTEGER,
                "date" to TEXT
        )

        db.createTable("EveningData", true,
                "Date" to TEXT + PRIMARY_KEY,
                "timestamp" to TEXT,
                "dailyPerformance" to INTEGER,
                "dailyFatigue" to INTEGER,
                "dailySleepAmount" to TEXT,
                "dailySleepTime" to INTEGER,
                "dailyAlcohol" to TEXT,
                "bedtime" to TEXT,
                "eveningFeeling" to INTEGER
                )

        db.createTable("MorningData", true,
                "Date" to TEXT + PRIMARY_KEY,
                "timestamp" to TEXT,
                "sleepQuality" to INTEGER,
                "morningFeeling" to INTEGER,
                "timeUntilSleepStart" to INTEGER,
                "timesAwakeDuringNight" to INTEGER,
                "totalTimeAwakeDuringNight" to INTEGER,
                "sleepEnd" to TEXT,
                "outOfBedTime" to TEXT,
                "sleepTime" to INTEGER,
                "sleepTimeString" to TEXT)

        db.createTable("RIS", true,
                "Date" to TEXT + PRIMARY_KEY,
                "timestamp" to TEXT,
                "avgSleepStart" to TEXT,
                "avgSleepEnd" to TEXT,
                "avgTimeUntilAsleep" to INTEGER,
                "avgSleepTime" to INTEGER,
                "sleepInterruption" to INTEGER,
                "earlySleepEnd" to INTEGER,
                "noiseAffinity" to INTEGER,
                "feltSleepQuality" to INTEGER,
                "sleepPondering" to INTEGER,
                "sleepFear" to INTEGER,
                "feltPerformance" to INTEGER,
                "generalMedication" to INTEGER,
                "score" to INTEGER,
                "scoreText" to TEXT
                )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }




    // check if evening entry exists. If yes: update; if no: insert
    fun upsertEveningData(eveningData: EveningData){
        Log.d(TAG, "upserting evening data")
        use{
            select("EveningData")
                    .whereArgs("Date == {date}", "date" to eveningData.date)
                    .exec{
                        if(this.count == 0){
                            insertEveningData(eveningData)
                        }else {
                            updateEveningData(eveningData)
                        }
                    }
        }

    }

    //insert evening data
    fun insertEveningData(eveningData: EveningData){
        Log.d(TAG, "inserting evening data")

        use {
            insert("EveningData",
                    "Date" to eveningData.date,
                    "timestamp" to eveningData.dateInMillis,
                    "dailyPerformance" to eveningData.dailyPerformance,
                    "dailyFatigue" to eveningData.dailyFatigue,
                    "dailySleepAmount" to eveningData.dailySleepAmount,
                    "dailySleepTime" to eveningData.dailySleepTime,
                    "dailyAlcohol" to eveningData.dailyAlcohol.toString(),
                    "bedtime" to eveningData.bedTime,
                    "eveningFeeling" to eveningData.eveningFeeling)
        }
    }

    // update evening data
    fun updateEveningData(eveningData: EveningData){
        Log.d(TAG, "updating evening data")

        use {
            update("EveningData",
                    "dailyPerformance" to eveningData.dailyPerformance,
                    "dailyFatigue" to eveningData.dailyFatigue,
                    "dailySleepAmount" to eveningData.dailySleepAmount,
                    "dailySleepTime" to eveningData.dailySleepTime,
                    "dailyAlcohol" to eveningData.dailyAlcohol.toString(),
                    "bedtime" to eveningData.bedTime,
                    "eveningFeeling" to eveningData.eveningFeeling
            )
                    .whereArgs(
                            "Date = {date}", "date" to eveningData.date)
                    .exec()
        }
    }


    // check if morning entry exists. If yes: update; if no: insert
    fun upsertMorningData(morningData: MorningData){
        Log.d(TAG, "upserting morning data")
        use{
            select("MorningData")
                    .whereArgs("Date == {date}", "date" to morningData.date)
                    .exec{
                        if(this.count == 0){
                            insertMorningData(morningData)
                        }else {
                            updateMorningData(morningData)
                        }
                    }
        }

    }

    //insert morning data
    fun insertMorningData(morningData: MorningData){
        Log.d(TAG, "inserting morning data")

        use {
            insert("MorningData",
                    "Date" to morningData.date,
                    "timestamp" to morningData.dateInMillis,
                    "sleepQuality" to morningData.sleepQuality,
                    "morningFeeling" to morningData.morningFeeling,
                    "timeUntilSleepStart" to morningData.timeUntilSleepStart,
                    "timesAwakeDuringNight" to morningData.timesAwakeDuringNight,
                    "totalTimeAwakeDuringNight" to morningData.totalTimeAwakeDuringNight,
                    "sleepEnd" to morningData.sleepEnd,
                    "outOfBedTime" to morningData.outOfBedTime,
                    "sleepTime" to morningData.sleepTime,
                    "sleepTimeString" to morningData.sleepTimeString)
        }
    }

    //updating morning data
    fun updateMorningData(morningData: MorningData){
        Log.d(TAG, "updating morning data")

        use {
            update("MorningData",
                    "sleepQuality" to morningData.sleepQuality,
                    "morningFeeling" to morningData.morningFeeling,
                    "timeUntilSleepStart" to morningData.timeUntilSleepStart,
                    "timesAwakeDuringNight" to morningData.timesAwakeDuringNight,
                    "totalTimeAwakeDuringNight" to morningData.totalTimeAwakeDuringNight,
                    "sleepEnd" to morningData.sleepEnd,
                    "outOfBedTime" to morningData.outOfBedTime,
                    "sleepTime" to morningData.sleepTime,
                    "sleepTimeString" to morningData.sleepTimeString)
                    .whereArgs(
                            "Date = {date}", "date" to morningData.date)
                    .exec()
        }
    }

    //return all evening data sorted by date
    fun getAllEveningData() : MutableList<EveningData>{
        var eveningDataList = mutableListOf<EveningData>()

        use{
            select("EveningData" )
                    .orderBy("timestamp", SqlOrderDirection.ASC)
                    .exec{
                        parseList(object: RowParser<Unit> {
                            override fun parseRow(columns: Array<Any?>) {
                                var eveningData = EveningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(), columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), mutableListOf(columns[6].toString()), columns[7].toString(), columns[8].toString().toInt())
                                eveningDataList.add(eveningData)
                            }
                        })
                    }
        }

        return eveningDataList
    }


    //return single evening entry
    fun getEveningByDate(eveningDate:String): EveningData? {
        return use{
            select("EveningData")
                    .whereArgs("Date = {eveningDate}",
                            "eveningDate" to eveningDate)
                    .exec{
                        if(this.count == 0){
                            null
                        }else{
                            parseOpt(object: RowParser<EveningData>{
                                override fun parseRow(columns: Array<Any?>):EveningData {
                                    return EveningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(), columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), mutableListOf(columns[6].toString()), columns[7].toString(), columns[8].toString().toInt())
                                }
                            })

                        }
                    }
        }
    }

    // return single morning entry
    fun getMorningByDate(morningDate:String): MorningData? {
        return use{
            select("MorningData")
                    .whereArgs("Date = {morningDate}",
                            "morningDate" to morningDate)
                    .exec{
                        if(this.count == 0){
                            null
                        }else{
                            parseOpt(object: RowParser<MorningData>{
                                override fun parseRow(columns: Array<Any?>):MorningData {
                                    return MorningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(),columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString(), columns[8].toString(), mutableListOf(), columns[9].toString().toInt(), columns[10].toString())
                                }
                            })

                        }
                    }
        }
    }

    // return all morning entries sorted by date
    fun getAllMorningData(): MutableList<MorningData> {
        var morningDataList = mutableListOf<MorningData>()
        use{
            select("MorningData" )
                    .orderBy("timestamp", SqlOrderDirection.ASC)
                    .exec{
                        parseList(object: RowParser<Unit> {
                            override fun parseRow(columns: Array<Any?>) {
                                var morningData = MorningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(),columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString(), columns[8].toString(), mutableListOf(), columns[9].toString().toInt(), columns[10].toString())
                                morningDataList.add(morningData)
                            }
                        })
                    }
        }
        return morningDataList

    }


    // get all Morning entries from the last 4 weeks
    fun getPreviousFourWeeksMornings(): MutableList<MorningData>{
        var morningDataList = mutableListOf<MorningData>()
        use{
            select("MorningData" )
                    .whereArgs("timestamp >= {last4Weeks}", "last4Weeks" to System.currentTimeMillis() - 2419200000)
                    .orderBy("timestamp", SqlOrderDirection.ASC)
                    .exec{
                        parseList(object: RowParser<Unit> {
                            override fun parseRow(columns: Array<Any?>) {
                                var morningData = MorningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(),columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString(), columns[8].toString(), mutableListOf(), columns[9].toString().toInt(), columns[10].toString())
                                morningDataList.add(morningData)
                            }
                        })
                    }
        }
        return morningDataList
    }

    // get all Evening entries from the last 4 weeks
    fun getPreviousFourWeeksEvenings(): MutableList<EveningData>{
        var eveningDataList = mutableListOf<EveningData>()
        use{
            select("EveningData" )
                    .whereArgs("timestamp >= {last4Weeks}", "last4Weeks" to System.currentTimeMillis() - 2419200000)
                    .orderBy("timestamp", SqlOrderDirection.ASC)
                    .exec{
                        parseList(object: RowParser<Unit> {
                            override fun parseRow(columns: Array<Any?>) {
                                var eveningData = EveningData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString().toInt(), columns[3].toString().toInt(), columns[4].toString().toInt(), columns[5].toString().toInt(), mutableListOf(columns[6].toString()), columns[7].toString(), columns[8].toString().toInt())
                                eveningDataList.add(eveningData)
                            }
                        })
                    }
        }
        return eveningDataList
    }


    // check if RIS entry exists. If yes: update; if no: insert
    fun upsertRISData(risData: RISData){
        Log.d(TAG, "upserting ris data")
        use{
            select("RIS")
                    .whereArgs("Date == {date}", "date" to risData.date)
                    .exec{
                        if(this.count == 0){
                            insertRISData(risData)
                        }else {
                            updateRISData(risData)
                        }
                    }
        }

    }

    //insert RIS entry
    private fun insertRISData(risData: RISData){
        Log.d(TAG, "inserting ris data")

        use {
            insert("RIS",
                    "Date" to risData.date,
                    "timestamp" to risData.dateInMillis,
                    "avgSleepStart" to risData.avgSleepStart,
                    "avgSleepEnd" to risData.avgSleepEnd,
                    "avgTimeUntilAsleep" to risData.avgTimeUntilAsleep,
                    "avgSleepTime" to risData.avgSleepTime,
                    "sleepInterruption" to risData.sleepInterruption,
                    "earlySleepEnd" to risData.earlySleepEnd,
                    "noiseAffinity" to risData.noiseAffinity,
                    "feltSleepQuality" to risData.feltSleepQuality,
                    "sleepPondering" to risData.sleepPondering,
                    "sleepFear" to risData.sleepFear,
                    "feltPerformance" to risData.feltPerformance,
                    "generalMedication" to risData.medication,
                    "score" to risData.score,
                    "scoreText" to risData.scoreText
            )
        }
    }

    //update RIS entry
    private fun updateRISData(risData: RISData){
        Log.d(TAG, "updating night data")

        use {
            update("RIS",
                    "avgSleepStart" to risData.avgSleepStart,
                    "avgSleepEnd" to risData.avgSleepEnd,
                    "avgTimeUntilAsleep" to risData.avgTimeUntilAsleep,
                    "avgSleepTime" to risData.avgSleepTime,
                    "sleepInterruption" to risData.sleepInterruption,
                    "earlySleepEnd" to risData.earlySleepEnd,
                    "noiseAffinity" to risData.noiseAffinity,
                    "feltSleepQuality" to risData.feltSleepQuality,
                    "sleepPondering" to risData.sleepPondering,
                    "sleepFear" to risData.sleepFear,
                    "feltPerformance" to risData.feltPerformance,
                    "generalMedication" to risData.medication,
                    "score" to risData.score,
                    "scoreText" to risData.scoreText
            )
                    .whereArgs(
                            "Date = {date}", "date" to risData.date)
                    .exec()
        }
    }

    //get single RIS entry
    fun getRISByDate(risDate:String): RISData?{
        return use{
            select("RIS")
                    .whereArgs("Date = {risDate}",
                            "risDate" to risDate)
                    .exec{
                        if(this.count == 0){
                            null
                        }else{
                            parseOpt(object: RowParser<RISData>{
                                override fun parseRow(columns: Array<Any?>):RISData{
                                    return RISData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString(),columns[3].toString(), columns[4].toString().toInt(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString().toInt(), columns[8].toString().toInt(), columns[9].toString().toInt(), columns[10].toString().toInt(), columns[11].toString().toInt(), columns[12].toString().toInt(), columns[13].toString().toInt(), columns[14].toString().toInt(), columns[15].toString())
                                }
                            })

                        }
                    }
        }
    }

    //get all RIS entry sorted by date
    fun getAllRISData(): MutableList<RISData> {
        var risDataList = mutableListOf<RISData>()
        use{
            select("RIS" )
                    .orderBy("timestamp", SqlOrderDirection.ASC)
                    .exec{
                        parseList(object: RowParser<Unit> {
                            override fun parseRow(columns: Array<Any?>) {
                                var risData = RISData(columns[0].toString(), columns[1].toString().toLong(), columns[2].toString(),columns[3].toString(), columns[4].toString().toInt(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString().toInt(), columns[8].toString().toInt(), columns[9].toString().toInt(), columns[10].toString().toInt(), columns[11].toString().toInt(), columns[12].toString().toInt(), columns[13].toString().toInt(), columns[14].toString().toInt(), columns[15].toString())
                                risDataList.add(risData)
                            }
                        })
                    }
        }
        return risDataList
    }


    // check if personal exists. If yes: update; if no: insert
    fun upsertPersonalData(personalInfoData: PersonalInfoData){
        Log.d(TAG, "upserting personal data")
        use{
            select("PersonalInfo")
            .exec{
                if(this.count == 0){
                    insertPersonalData(personalInfoData)
                }else {
                    updatePersonalData(personalInfoData)
                }
            }
        }

    }

    //insert Personal entry
    private fun insertPersonalData(personalInfoData: PersonalInfoData){
        Log.d(TAG, "inserting personal data")
        use {
            insert("PersonalInfo",
                    "Name" to personalInfoData.name,
                    "birthday" to personalInfoData.birthday,
                    "sayAsleep" to personalInfoData.sleepPattern,
                    "stayAwake" to personalInfoData.stayAwake,
                    "otherSleepProblems" to personalInfoData.otherSleepProblems,
                    "amountPerWeek" to personalInfoData.amountPerWeek,
                    "sinceWhenYears" to personalInfoData.sinceWhenYears,
                    "sinceWhenMonths" to personalInfoData.sinceWhenMonths,                    "otherDiseases" to personalInfoData.otherDiseases,
                    "generalMedication" to personalInfoData.generalMedication,
                    "profession" to personalInfoData.profession,
                    "height" to personalInfoData.height,
                    "weight" to personalInfoData.weight,
                    "numberHouseholdmembers" to personalInfoData.numberHouseholdMembers,
                    "numberChildren" to personalInfoData.numberChildren,
                    "date" to personalInfoData.date

            )
        }
    }

    //update Personal entry
    private fun updatePersonalData(personalInfoData: PersonalInfoData){
        Log.d(TAG, "updating personal data")

        use {
            update("PersonalInfo",
                    "Name" to personalInfoData.name,
                    "birthday" to personalInfoData.birthday,
                    "sayAsleep" to personalInfoData.sleepPattern,
                    "stayAwake" to personalInfoData.stayAwake,
                    "otherSleepProblems" to personalInfoData.otherSleepProblems,
                    "amountPerWeek" to personalInfoData.amountPerWeek,
                    "sinceWhenYears" to personalInfoData.sinceWhenYears,
                    "sinceWhenMonths" to personalInfoData.sinceWhenMonths,
                    "otherDiseases" to personalInfoData.otherDiseases,
                    "generalMedication" to personalInfoData.generalMedication,
                    "profession" to personalInfoData.profession,
                    "height" to personalInfoData.height,
                    "weight" to personalInfoData.weight,
                    "numberHouseholdmembers" to personalInfoData.numberHouseholdMembers,
                    "numberChildren" to personalInfoData.numberChildren,
                    "date" to personalInfoData.date
            )
            .whereArgs(
                    "Name = {name}", "name" to personalInfoData.name)
            .exec()
        }
    }

    //get Personal Info
    fun getPersonalInfo(): PersonalInfoData?{
        return use{
            select("PersonalInfo")
            .exec{
                if(this.count == 0){
                    null
                }else{
                    parseSingle(object: RowParser<PersonalInfoData>{
                        override fun parseRow(columns: Array<Any?>):PersonalInfoData{
                            return PersonalInfoData(columns[0].toString(), columns[1].toString(), columns[2].toString().toInt(),columns[3].toString().toInt(), columns[4].toString(), columns[5].toString().toInt(), columns[6].toString().toInt(), columns[7].toString().toInt(), columns[8].toString(), columns[9].toString(), columns[10].toString(), columns[11].toString().toInt(), columns[12].toString().toInt(), columns[13].toString().toInt(), columns[14].toString().toInt(), columns[15].toString())
                        }
                    })

                }
            }
        }
    }
}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)