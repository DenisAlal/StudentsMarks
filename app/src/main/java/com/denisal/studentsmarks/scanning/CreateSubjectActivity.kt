package com.denisal.studentsmarks.scanning

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.teacherID
import java.util.*

class CreateSubjectActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val courses = arrayOf<String?>("Выберите тип занятия","Лекция", "Практические занятия")
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    private var startHour = getTime(cal.get(Calendar.HOUR_OF_DAY).toString())
    private var startMinute = getTime(cal.get(Calendar.MINUTE).toString())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Успеваемость"
        setContentView(R.layout.activity_create_subject)
        val db = GetFromDB()
        Log.e("","$teacherID")
        val studData: MutableList<SubjData> = db.getDataForSpinner()
        var arraySpinner: List<String> = arrayListOf()
        // select subj spinner

        if (studData.isNotEmpty()) {
            for (index in studData.indices) {
                val value = studData[index]
                arraySpinner = arrayListOf(value.name)
            }
        }

        if(studData.isEmpty()) {
            val spinner: Spinner = findViewById(R.id.spinnerSubj)
            spinner.isEnabled = false
            Log.e("123", "error")
//////////////////////////////////////////остановился здесь, сделать разбор массива
        } else {
            val spinSubj = findViewById<Spinner>(R.id.spinnerSubj)
            spinSubj.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, arraySpinner)
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinSubj.adapter = add
        }


        val tz = cal.timeZone
        Log.e("","$tz")
        // type subj spinner
        val spinType = findViewById<Spinner>(R.id.spinnerSubjType)
        spinType.onItemSelectedListener = this
        val add: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, courses)
        add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinType.adapter = add
        val pickerDateBtn: Button = findViewById(R.id.pickDate)
        val pickerTimeBtn: Button = findViewById(R.id.pickTime)
        val text: TextView = findViewById(R.id.text3G)
        val text2: TextView = findViewById(R.id.text4)
        text.text = "Выберите дату занятия, сейчас указана дата, $year-$month-$day"
        text2.text = "Выберите время занятия, сейчас указано время, $startHour:$startMinute"
        pickerDateBtn.setOnClickListener {
            DatePickerDialog(this,  { view, myYear, myMonth, myDayOfMonth ->
                year = myYear
                month = getMonth(myMonth.toString())
                day = getDay(myDayOfMonth.toString())
                text.text = "Вы выбрали дату, $year-$month-$day"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        pickerTimeBtn.setOnClickListener{
            TimePickerDialog(this, {view, myHour, myMinute ->
                val newHour = getTime(myHour.toString())
                val newMinute = getTime(myMinute.toString())
                Log.e("123", "$newHour:$newMinute")
                text.text = "Вы выбрали время, $newHour:$newMinute"
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun getMonth(month: String): String {
        val newMon = month.toInt() + 1
        var newMonth = newMon.toString()
        if (month.length < 2) {
             newMonth = "0$newMonth"
        }
        return newMonth
    }
    private fun getDay(day: String): String {
        val newd = day.toInt()
        var newDay = newd.toString()
        if (day.length < 2) {
            newDay = "0$newDay"
        }
        return newDay
    }
    private fun getTime(day: String): String {
        val newt = day.toInt()
        var newTime = newt.toString()
        if (day.length < 2) {
            newTime = "0$newTime"
        }
        return newTime
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            //Toast.makeText(applicationContext, "Выберите тип занятия", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(applicationContext,
                courses[position],
                Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}