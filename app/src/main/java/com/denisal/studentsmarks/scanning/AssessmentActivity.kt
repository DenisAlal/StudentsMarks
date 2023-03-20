package com.denisal.studentsmarks.scanning

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.denisal.studentsmarks.StudentsData
import com.denisal.studentsmarks.databinding.ActivityAssessmentBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.dbfunctions.InsertToDB
import com.denisal.studentsmarks.id_task
import com.denisal.studentsmarks.listStudent
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentBinding
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    val db = GetFromDB()
    private var check: MutableList<StudentsData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Выставление оценки"
        if(listStudent.isNotEmpty()) {
            binding.textFIO.text = "ФИО: " + listStudent[0]
            binding.textGroup.text = "Группа: " + listStudent[1]
            check = db.checkStudent(listStudent[0], listStudent[1])
            Log.e("check_student", check.toString())
        }
        binding.textDate.text = "Выберите дату сдачи, сейчас указана дата, $year-$month-$day"
        binding.dateBtn.setOnClickListener {
            DatePickerDialog(this,  { view, myYear, myMonth, myDayOfMonth ->
                year = myYear
                month = getMonth(myMonth.toString())
                day = getDay(myDayOfMonth.toString())
                binding.textDate.text = "Вы выбрали дату, $year-$month-$day"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.saveAssessment.setOnClickListener{
            val text = binding.assessment.text
            if(listStudent.isNotEmpty() && text.isNotEmpty()) {
                if (check.isNotEmpty()) {
                    val insert = InsertToDB()
                    val dateIns = "$year-$month-$day"
                    val checkInsert = insert.insertAssessment(text.toString(),
                        dateIns, check[0].id, id_task)
                    if (checkInsert) {
                        Toast.makeText(applicationContext, "Данные успешно сохранены", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "Ошибка загрузки данных", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Заполните поле с оценкой", Toast.LENGTH_LONG).show()
            }
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