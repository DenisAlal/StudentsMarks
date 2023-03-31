package com.denisal.studentsmarks.scanning.activites

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.dbfunctions.InsertToDB
import java.util.*
class CreateSubjectActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    private var hour = getTime(cal.get(Calendar.HOUR_OF_DAY).toString())
    private var minute = getTime(cal.get(Calendar.MINUTE).toString())
    private var subjArray: MutableList<SubjData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Успеваемость"
        setContentView(R.layout.activity_create_subject)
        val db = GetFromDB()
        subjArray = db.getDataCourse()

        if (subjArray.isNotEmpty()) {
            val arrayForSpinnerSubj: ArrayList<String> = arrayListOf()
            for (index in subjArray.indices) {
                val value = subjArray[index]
                arrayForSpinnerSubj.add(value.name)
            }
            val spinSubj: Spinner = findViewById(R.id.spinnerSubj)
            spinSubj.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,
                arrayForSpinnerSubj as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinSubj.adapter = add

        } else {
            val spinnerEmpty: LinearLayout = findViewById(R.id.emptyArray)
            spinnerEmpty.isVisible = false
            val builderError = AlertDialog.Builder(this)
                .setTitle("Загрузка завершилась ошибкой")
                .setMessage("Ваш список предметов пуст! " +
                        "Для того чтобы создать занятие сначала создайте предмет!")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderError.setPositiveButton("OK"){ _, _ ->
                finish()
            }
        }
        init()

    }
    private fun init() {
        val pickerDateBtn: Button = findViewById(R.id.pickDate)
        val pickerTimeBtn: Button = findViewById(R.id.pickTime)
        val save: Button = findViewById(R.id.saveSubject)
        val text: TextView = findViewById(R.id.text3G)
        val text2: TextView = findViewById(R.id.text4)
        val getText: EditText = findViewById(R.id.gradeTopic)
        val insertToDB = InsertToDB()

        text.text = "Выберите дату занятия, сейчас указана дата, $year-$month-$day"
        text2.text = "Выберите время занятия, сейчас указано время, $hour:$minute"


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
                hour = getTime(myHour.toString())
                minute = getTime(myMinute.toString())
                text2.text = "Вы выбрали время, $hour:$minute"
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        save.setOnClickListener{
            if(getText.text.isNotEmpty()) {
                val spinnerType: Spinner = findViewById(R.id.spinnerSubjType)
                val spinnerSubj: Spinner = findViewById(R.id.spinnerSubj)
                val selectedType = spinnerType.selectedItem.toString()
                val idSelectedSubj = spinnerSubj.selectedItemId
                val date = "$year-$month-$day"
                val time = "$hour:$minute"
                val courseId = subjArray[idSelectedSubj.toInt()].id
                val check = insertToDB.insertLeson(getText.text.toString(), date, time, courseId, selectedType)
                if (check) {
                    val mBuilderSuccess = AlertDialog.Builder(this)
                        .setTitle("Занятие добавлено")
                        .setMessage("Добавить еще одно занятие?")
                        .setIcon(R.drawable.baseline_check_circle_24)
                    mBuilderSuccess.setPositiveButton("Да"){ _, _ ->
                        getText.setText("")

                    }
                    mBuilderSuccess.setNegativeButton("Нет"){ _, _ ->
                        getText.setText("")
                        finish()
                    }
                    val alertDialogError: AlertDialog = mBuilderSuccess.create()
                    alertDialogError.show()}
            } else {
                Toast.makeText(applicationContext, "Введите тему занятия!", Toast.LENGTH_LONG).show()
            }
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
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val subjTypeArray: MutableList<String> = mutableListOf()
        if (parent.id == R.id.spinnerSubj) {
            val check = subjArray[position]
            if(check.lecture == 1) {
                subjTypeArray.add("Лекция")
            }
            if (check.practic == 1) {
                subjTypeArray.add("Практическое занятие")
            }
            val spinType = findViewById<Spinner>(R.id.spinnerSubjType)
            if (subjTypeArray.isNotEmpty()) {
                spinType.onItemSelectedListener = this
                val add: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,
                    subjTypeArray as List<Any?>)
                add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinType.adapter = add
            } else {
                spinType.isVisible = false
            }


        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}