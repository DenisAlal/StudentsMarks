package com.denisal.studentsmarks.scanning

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.denisal.studentsmarks.R
import java.util.*

class TrafficScanningActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val courses = arrayOf<String?>("Выберите тип занятия","Лекция", "Практические занятия")
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_scanning)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Посещаемость"
        val spin = findViewById<Spinner>(R.id.spinnerSubjTrafficType)
        spin.onItemSelectedListener = this
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            courses)

        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = ad


        val pickerDateBtn: Button = findViewById(R.id.pickDateTraffic)
        val text: TextView = findViewById(R.id.text3T)

        text.text = "Выберите дату занятия, сейчас указана дата, $day.$month.$year"
        pickerDateBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                year = myear
                month = getMonth(mmonth.toString())
                day = getDay(mdayOfMonth.toString())
                text.text = "Вы выбрали дату, $day.$month.$year"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
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
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            Toast.makeText(applicationContext,
                "Выберите тип занятия",
                Toast.LENGTH_LONG)
                .show()

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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}