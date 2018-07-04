package com.example.adrianweniger.sleepinspector

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.app.DownloadManager
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import com.example.adrianweniger.sleepinspector.DataClasses.EveningData
import com.example.adrianweniger.sleepinspector.DataClasses.MorningData
import com.example.adrianweniger.sleepinspector.DataClasses.RISData
import com.example.adrianweniger.sleepinspector.database.database
import kotlinx.android.synthetic.main.layout_pdf_report_cover.view.*
import kotlinx.android.synthetic.main.layout_pdf_report_ris_table.view.*
import kotlinx.android.synthetic.main.layout_pdf_report_sleepdiary_table.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Creates PDF reports
 * checks for needed permissions
 */
class PDFCreator {

    val TAG = "PDF CREATOR"

    //set up page dimensions
    private val pageInfoA4Landscape = PageInfo.Builder(842, 595, 1).create()
    private val pageInfoA4Portrait = PageInfo.Builder(595, 842, 1).create()

    //create report
    fun createPDFReport(context :Context){
        var document = PdfDocument()

        //get and add Report Cover to file
        var reportCoverContent = generateCoverContent(context)
        var reportCoverPage = document.startPage(pageInfoA4Portrait)
        val measureWidth = View.MeasureSpec.makeMeasureSpec(reportCoverPage.canvas.width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(reportCoverPage.canvas.height, View.MeasureSpec.EXACTLY)
        reportCoverContent.measure(measureWidth, measuredHeight)
        reportCoverContent.layout(0, 0, reportCoverPage.canvas.width, reportCoverPage.canvas.height)
        reportCoverContent.draw(reportCoverPage.canvas)
        document.finishPage(reportCoverPage)

        //get RIS Data
        var risContent = generateRISContent(context)
        for (table in risContent){
            var page = document.startPage(pageInfoA4Landscape)
            val measureWidth = View.MeasureSpec.makeMeasureSpec(page.canvas.width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(page.canvas.height, View.MeasureSpec.EXACTLY)
            table.measure(measureWidth, measuredHeight)
            table.layout(0, 0, page.canvas.width, page.canvas.height)
            table.draw(page.canvas)
            document.finishPage(page)
        }

        //get and add Sleepdiary tables to file
        val tables = getSleepdiaryContent(context)
        for (table in tables){
            var page = document.startPage(pageInfoA4Landscape)
            val measureWidth = View.MeasureSpec.makeMeasureSpec(page.canvas.width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(page.canvas.height, View.MeasureSpec.EXACTLY)
            table.measure(measureWidth, measuredHeight)
            table.layout(0, 0, page.canvas.width, page.canvas.height)
            table.draw(page.canvas)
            document.finishPage(page)
        }



        // write to file
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SleepInspector_Report_" + System.currentTimeMillis() + ".pdf")
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val title = "Sleepinspector Report " + System.currentTimeMillis()
            val descr = "Sleepinspector Report"

            // Workaround: download manager needed to display file in Download Folder. Otherwise the File is not displayed
            // needs internet permissions but DOES NOT (!) connect to the internet - no data is communicated to the outside world
            dm.addCompletedDownload(title, descr, true, "text/pdf",file.absolutePath, file.length(),  true)
            Toast.makeText(context, "Auswertung im Downloadordner gespeichert", Toast.LENGTH_LONG).show()

        } catch (e: IOException) {
            Log.e(TAG, e.toString())
            throw RuntimeException("Error generating file", e)
        }
    }

    // verify permissions needed to save report to Download Folder
    fun verifyStoragePermissions(activity: Activity):Boolean {
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
            val requestCode = 200
            ActivityCompat.requestPermissions(activity, permissions, requestCode)

            if (permission == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                false
            }
        }
        return true
    }

    // generate Cover for the report
    // Cover includes Personal Data
    private fun generateCoverContent(context: Context): View {
        val inflater = LayoutInflater.from(context)
        var cover = inflater.inflate(R.layout.layout_pdf_report_cover, null).findViewById<LinearLayout>(R.id.cover)
        cover.report_date.text = "vom ${SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(System.currentTimeMillis())}"

        var personalInfo = context.database.getPersonalInfo()

        if(personalInfo == null){
            cover.name.text = "Keine Angaben zur Person angegeben"
            cover.birthday.text = ""
            cover.occupation.text = ""
            cover.weight.text = ""
            cover.height_info.text = ""
            cover.householdmembers.text = ""
            cover.children.text = ""
            cover.sleep_pattern.text = ""
            cover.stay_awake_problem.text = ""
            cover.problem_frequency.text = ""
            cover.since_when.text = ""
            cover.other_sleep_problems.text = ""
            cover.other_diseases.text = ""
            cover.general_medication.text = ""
        }else{
            cover.name.text = personalInfo.name
            cover.birthday.text = personalInfo.birthday
//            cover.occupation.text = "Beruf": personalInfo.occupation
            cover.weight.text = "${personalInfo.weight} kg"
            cover.height_info.text = "${personalInfo.height}cm groß,"
            cover.householdmembers.text = "${personalInfo.numberHouseholdMembers} Haushaltsmitglieder"
            cover.children.text = "davon ${personalInfo.numberChildren.toString()} Kinder"
            cover.sleep_pattern.text = if(personalInfo.sleepPattern != 0) "Probleme beim Ein- und Durchschlafen: JA" else "Probleme beim Ein- und Durchschlafen: NEIN"
            cover.stay_awake_problem.text = if(personalInfo.stayAwake != 0) "Probleme tagsüber wach zu bleiben: JA" else "Probleme tägsüber wach zu bleiben: NEIN"
            cover.problem_frequency.text = "Häufigkeit, mit der die Probleme unter der Woche auftauchen: ${personalInfo.amountPerWeek} mal pro Woche"
            cover.since_when.text = "Die Probleme bestehen seit ${personalInfo.sinceWhenYears} Jahren und ${personalInfo.sinceWhenMonths} Monaten"
            cover.other_sleep_problems.text = "Andere Schlafprobleme: ${personalInfo.otherSleepProblems}"
            cover.other_diseases.text = "Andere Krankheiten: ${personalInfo.otherDiseases}"
            cover.general_medication.text = "Zur Zeit eingenommene Medikamente: ${personalInfo.generalMedication}"
        }
        return cover
    }


    // below methods generate table with all the RIS Data
    private fun generateRISContent(context: Context): ArrayList<View> {
        var risDataList = context.database.getAllRISData()
        val inflater = LayoutInflater.from(context)
        val risTables = arrayListOf<View>()

        for ((rows, ris) in risDataList.withIndex()){

            if(rows % 21 == 0){
                val table = inflater.inflate(R.layout.layout_pdf_report_ris_table, null).findViewById<LinearLayout>(R.id.pdf_table)
                risTables.add(table)
            }

            var row = generateRISRow(context, ris)
            risTables[rows/21].ris_table.addView(row)

        }
        return risTables
    }

    private fun generateRISRow(context: Context, risData: RISData): View {

        val likert = arrayListOf("nie", "selten", "manchmal", "meistens", "immer")

        var tableRow = TableRow(context)
        var date = createTableTV(context, 64, 7f)
        var avgSleepBorders = createTableTV(context, 64, 7f)
        var avgTimeUntilAsleep = createTableTV(context, 64, 7f)
        var avgSleepTime = createTableTV(context, 64, 7f)
        var sleepInterruption = createTableTV(context, 64, 7f)
        var earlySleepEnd = createTableTV(context, 64, 7f)
        var noiseAffinity = createTableTV(context, 64, 7f)
        var feltSleepQuality = createTableTV(context, 64, 7f)
        var sleepPondering = createTableTV(context, 64, 7f)
        var sleepFear = createTableTV(context, 64, 7f)
        var feltPerformance = createTableTV(context, 64, 7f)
        var medication = createTableTV(context, 64, 7f)
        var result = createTableTV(context, 64, 7f)

        var score = risData.avgTimeUntilAsleep + risData.avgSleepTime + risData.sleepInterruption + risData.earlySleepEnd + risData.noiseAffinity + risData.noiseAffinity + risData.sleepPondering + risData.sleepFear + risData.feltPerformance + risData.medication
        var scoreText = when (score) {
            in 0..12 -> "unauffällig"
            in 13..24 -> "auffällig"
            in 25..40 -> "ausgeprägt"
            else -> ""
        }

        tableRow.layoutParams = TableLayout.LayoutParams(442, TableLayout.LayoutParams.WRAP_CONTENT)
        date.text = risData.date
        avgSleepBorders.text = "${risData.avgSleepStart} - ${risData.avgSleepEnd} Uhr"
        avgTimeUntilAsleep.text = risData.avgTimeUntilAsleep.toString()
        avgSleepTime.text = risData.avgSleepTime.toString()
        sleepInterruption.text = likert[risData.sleepInterruption]
        earlySleepEnd.text = likert[risData.earlySleepEnd]
        noiseAffinity.text = likert[risData.noiseAffinity]
        feltSleepQuality.text = likert[risData.feltSleepQuality]
        sleepPondering.text = likert[risData.sleepPondering]
        sleepFear.text = likert[risData.sleepFear]
        feltPerformance.text = likert[risData.feltPerformance]
        medication.text = likert[risData.medication]
        result.text = scoreText


        tableRow.addView(date)
        tableRow.addView(avgSleepBorders)
        tableRow.addView(avgTimeUntilAsleep)
        tableRow.addView(avgSleepTime)
        tableRow.addView(sleepInterruption)
        tableRow.addView(earlySleepEnd)
        tableRow.addView(noiseAffinity)
        tableRow.addView(feltSleepQuality)
        tableRow.addView(sleepPondering)
        tableRow.addView(sleepFear)
        tableRow.addView(feltPerformance)
        tableRow.addView(medication)
        tableRow.addView(result)


        return tableRow

    }



    // below methods generate table with all the Sleepdiary Data
    private fun getSleepdiaryContent(context: Context): ArrayList<View> {
        var startWithEvening = false
        var eveningDataList = context.database.getAllEveningData()
        var morningDataList = context.database.getAllMorningData()

        if(eveningDataList.isEmpty() && morningDataList.isEmpty()){
            return arrayListOf()
        } else if (eveningDataList.isNotEmpty() && morningDataList.isEmpty()){
            startWithEvening = true
        } else if (eveningDataList.isEmpty() && morningDataList.isNotEmpty()){
            startWithEvening = false
        }

        if(eveningDataList[0].date.split("/")[0] < morningDataList[0].date.split("/")[0]){
            startWithEvening = true
        }
        var tables = createTables(eveningDataList, morningDataList, startWithEvening, context)

        return tables
    }

    private fun createTables (eveningData: MutableList<EveningData>, morningData: MutableList<MorningData>, startWithEvening:Boolean, context: Context) :ArrayList<View>{
        val inflater = LayoutInflater.from(context)
        val tables = arrayListOf<View>()
        var rows = 0

        if(startWithEvening){
            var currentDay = convertStringToDate(eveningData[0].date)
            while (eveningData.isNotEmpty() && morningData.isNotEmpty()){

                if(rows % 14 == 0){
                    val table = inflater.inflate(R.layout.layout_pdf_report_sleepdiary_table, null).findViewById<LinearLayout>(R.id.pdf_table)
                    tables.add(table)
                }

                var cal = Calendar.getInstance()
                cal.timeInMillis = currentDay.time
                cal.add(Calendar.DATE, 1)
                var nextDay = cal.time


                var evening = eveningData.find{ it.date == SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(currentDay.time))}
                var morning = morningData.find { it.date == SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(nextDay.time))}

                var eveningRow = View(context)
                var morningRow = View(context)

                if(evening != null && morning != null) {
                    eveningData.remove(evening)
                    eveningRow = createEveningRow(evening, null,context)

                    morningData.remove(morning)
                    morningRow = createMorningRow(morning, context)
                } else if(evening == null && morning != null) {
                    eveningRow = createEveningRow(null, currentDay,context)

                    morningData.remove(morning)
                    morningRow = createMorningRow(morning, context)
                } else if(evening != null && morning == null) {
                    morningRow = createMorningRow(null, context)

                    eveningData.remove(evening)
                    eveningRow = createEveningRow(evening, null, context)
                } else if (evening == null && morning == null) {
                    eveningRow = createEveningRow(null, currentDay, context)
                    morningRow = createMorningRow(null, context)
                }

                tables[rows/14].table_evening_data.addView(eveningRow)
                tables[rows/14].table_morning_data.addView(morningRow)
                currentDay = nextDay

                rows++
            }
        } else {

            var currentDay = convertStringToDate(morningData[0].date)

            while (eveningData.isNotEmpty() && morningData.isNotEmpty()){

                if(rows % 14 == 0){
                    val table = inflater.inflate(R.layout.layout_pdf_report_sleepdiary_table, null).findViewById<LinearLayout>(R.id.pdf_table)
                    tables.add(table)
                }

                var cal = Calendar.getInstance()
                cal.timeInMillis = currentDay.time
                cal.add(Calendar.DATE, -1)
                var previousDay = cal.time

                var morning = morningData.find { it.date == SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(currentDay.time))}
                var evening = eveningData.find{ it.date == SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(previousDay.time))}


                var eveningRow = View(context)
                var morningRow = View(context)

                if(evening != null && morning != null) {
                    eveningData.remove(evening)
                    eveningRow = createEveningRow(evening!!, null,context)

                    morningData.remove(morning)
                    morningRow = createMorningRow(morning!!, context)
                } else if(evening == null && morning != null) {
                    eveningRow = createEveningRow(null, previousDay,context)

                    morningData.remove(morning)
                    morningRow = createMorningRow(morning!!, context)
                } else if(evening != null && morning == null) {
                    morningRow = createMorningRow(null, context)

                    eveningData.remove(evening)
                    eveningRow = createEveningRow(evening!!, null, context)
                } else if (evening == null && morning == null) {
                    eveningRow = createEveningRow(null, previousDay, context)
                    morningRow = createMorningRow(null, context)
                }


                tables[rows/14].table_evening_data.addView(eveningRow)
                tables[rows/14].table_morning_data.addView(morningRow)


                var newCurrentDay = Calendar.getInstance()
                newCurrentDay.timeInMillis = currentDay.time
                newCurrentDay.add(Calendar.DATE, 1)
                currentDay = newCurrentDay.time

                rows++
            }

        }
        return tables
    }


    private fun convertStringToDate(dateString:String): Date {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN)
        return df.parse(dateString)
    }

    private fun createEveningRow(eveningData: EveningData?, givenDate :Date? ,context: Context) :View{

        var tableRow = TableRow(context)
        var date = createTableTV(context, 45, 7f)
        var dailyPerformance = createTableTV(context, 58, 7f)
        var dailyFatigue = createTableTV(context, 56, 7f)
        var dailySleep = createTableTV(context, 56, 7f)
        var dailyAlcohol = createTableTV(context, 56, 7f)
        var bedTime = createTableTV(context, 56, 7f)
        var eveningFeeling = createTableTV(context, 56, 7f)

        tableRow.layoutParams = TableLayout.LayoutParams(399, TableLayout.LayoutParams.WRAP_CONTENT)

        if(eveningData == null){
            date.text = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN).format(Date(givenDate!!.time))
            dailyPerformance.text = "---"
            dailyFatigue.text = "---"
            dailySleep.text = "---"
            dailyAlcohol.text = "---"
            bedTime.text = "---"
            eveningFeeling.text = "---"
        }else{
            date.text = eveningData.date
            dailyPerformance.text = ScaleWordings.dailyPerformanceParams[eveningData.dailyPerformance]
            dailyFatigue.text = ScaleWordings.dailyFatigueParams[eveningData.dailyFatigue]
            dailySleep.text = eveningData.dailySleepAmount.toString() + " min"
            dailyAlcohol.text = eveningData.dailyAlcohol[0].subSequence(1, eveningData.dailyAlcohol[0].length -1)
            bedTime.text = eveningData.bedTime
            eveningFeeling.text = ScaleWordings.eveningFeelingParams[eveningData.eveningFeeling]
        }

        tableRow.addView(date)
        tableRow.addView(dailyPerformance)
        tableRow.addView(dailyFatigue)
        tableRow.addView(dailySleep)
        tableRow.addView(dailyAlcohol)
        tableRow.addView(bedTime)
        tableRow.addView(eveningFeeling)

        return tableRow
    }

    private fun createMorningRow(morningData: MorningData?, context: Context): View{
        var tableRow = TableRow(context)
        var sleepQuality = createTableTV(context, 56, 7f)
        var morningFeeling = createTableTV(context, 56, 7f)
        var sleepStart = createTableTV(context, 56, 7f)
        var timesAwake = createTableTV(context, 56, 7f)
        var sleepEnd = createTableTV(context, 56, 7f)
        var outOfBed = createTableTV(context, 56, 7f)
        var medication = createTableTV(context, 56, 7f)
        var sleepTime = createTableTV(context, 56, 7f)


        tableRow.layoutParams = TableLayout.LayoutParams(442, TableLayout.LayoutParams.WRAP_CONTENT)

        if(morningData == null){
            sleepQuality.text = "---"
            morningFeeling.text = "---"
            sleepStart.text = "---"
            timesAwake.text = "---"
            sleepEnd.text = "---"
            outOfBed.text = "---"
            medication.text = "---"
            sleepTime.text = "---"

        }else{
            sleepQuality.text = ScaleWordings.sleepQualityParams[morningData.sleepQuality]
            morningFeeling.text = ScaleWordings.morningFeelingParams[morningData.morningFeeling]
            sleepStart.text = morningData.timeUntilSleepStart.toString() + " min"
            timesAwake.text = morningData.timesAwakeDuringNight.toString() + " min"
            sleepEnd.text = morningData.sleepEnd
            outOfBed.text = morningData.outOfBedTime
            medication.text = morningData.medication.toString()
            sleepTime.text = morningData.sleepTimeString

        }
        tableRow.addView(sleepQuality)
        tableRow.addView(morningFeeling)
        tableRow.addView(sleepStart)
        tableRow.addView(timesAwake)
        tableRow.addView(sleepEnd)
        tableRow.addView(outOfBed)
        tableRow.addView(medication)
        tableRow.addView(sleepTime)


        return tableRow
    }



    // programmatically create TextViews to display data
    private fun createTableTV(context: Context, width: Int, txtSize:Float) :TextView{
        var tv = TextView(context)

        tv.layoutParams = TableRow.LayoutParams(width, TableRow.LayoutParams.WRAP_CONTENT)
        tv.minHeight = 30
        tv.setTextColor(Color.BLACK)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize)
        tv.gravity = Gravity.CENTER_VERTICAL
        tv.gravity = Gravity.CENTER_HORIZONTAL

        return tv
    }





}