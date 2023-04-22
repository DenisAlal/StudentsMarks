package com.denisal.studentsmarks.scanning.activites

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.db.GetFromDB
import com.denisal.studentsmarks.db.InsertToDB

import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddAccountingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    private var subjArray: MutableList<SubjData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно создать занятие и перейти к сканированию студентов")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }


        val db = GetFromDB()
        subjArray = db.getDataCourse()

        val listSubj: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val spinnNumb: Spinner = findViewById(R.id.spinnerSubjNumber)
        spinnNumb.onItemSelectedListener = this
        val add: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, listSubj)
        add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnNumb.adapter = add

        if (subjArray.isNotEmpty()) {
            val arrayForSpinnerSubj: ArrayList<String> = arrayListOf()
            for (index in subjArray.indices) {
                val value = subjArray[index]
                arrayForSpinnerSubj.add(value.name)
            }
            val spinSubj: Spinner = findViewById(R.id.spinnerSubj)
            spinSubj.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                arrayForSpinnerSubj as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinSubj.adapter = add

        } else {
            val spinnerEmpty: LinearLayout = findViewById(R.id.emptyArray)
            spinnerEmpty.isVisible = false
            val builderError = AlertDialog.Builder(this)
                .setTitle("Загрузка завершилась ошибкой")
                .setMessage(
                    "Ваш список предметов пуст! " +
                            "Для того чтобы создать занятие сначала создайте предмет!"
                )
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderError.setPositiveButton("OK") { _, _ ->
                finish()
            }
            val alertDialogSuccess: AlertDialog = builderError.create()
            alertDialogSuccess.show()
        }
        init()

    }

    private fun init() {
        val pickerDateBtn: Button = findViewById(R.id.pickDate)
        val save: Button = findViewById(R.id.goScan)
        val text: TextView = findViewById(R.id.text3G)
        val getText: EditText = findViewById(R.id.gradeTopic)
        val insertToDB = InsertToDB()
        text.text = "Выберите дату занятия, сейчас указана дата, $year-$month-$day"


        pickerDateBtn.setOnClickListener {
            DatePickerDialog(
                this,
                { view, myYear, myMonth, myDayOfMonth ->
                    year = myYear
                    month = getMonth(myMonth.toString())
                    day = getDay(myDayOfMonth.toString())
                    text.text = "Вы выбрали дату, $year-$month-$day"
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        save.setOnClickListener {
            if (getText.text.isNotEmpty()) {
                val spinnerType: Spinner = findViewById(R.id.spinnerSubjType)
                val spinnerSubj: Spinner = findViewById(R.id.spinnerSubj)
                val spinnerNumb: Spinner = findViewById(R.id.spinnerSubjNumber)
                val selectedType = spinnerType.selectedItem.toString()
                val idSelectedSubj = spinnerSubj.selectedItemId
                val date = "$year-$month-$day"
                val subjNumb = spinnerNumb.selectedItem.toString()
                val courseId = subjArray[idSelectedSubj.toInt()].id
                    //val check = insertToDB.insertLeson( getText.text.toString(), date, subjNumb, courseId, selectedType )
                    Log.e("adwda", "aboba")
            } else {
                Toast.makeText(applicationContext, "Введите тему занятия!", Toast.LENGTH_LONG)
                    .show()
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
            if (check.lecture == 1) {
                subjTypeArray.add("Лекция")
            }
            if (check.practic == 1) {
                subjTypeArray.add("Практическое занятие")
            }
            val spinType = findViewById<Spinner>(R.id.spinnerSubjType)
            if (subjTypeArray.isNotEmpty()) {
                spinType.onItemSelectedListener = this
                val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this, android.R.layout.simple_spinner_item,
                    subjTypeArray as List<Any?>
                )
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