package com.denisal.studentsmarks.scanning

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.denisal.studentsmarks.databinding.ActivityChangeAssessmentBinding
import com.denisal.studentsmarks.dbfunctions.DeleteFromDb
import com.denisal.studentsmarks.dbfunctions.UpdateInDB
import java.util.*

class ChangeAssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeAssessmentBinding
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Изменение оценки"
        val update = UpdateInDB()
        val delete = DeleteFromDb()
        val strIDMark: Int = intent.getIntExtra("Assessment", -1)
        val strGroup: String? = intent.getStringExtra("Group")
        val strFIO: String? = intent.getStringExtra("FIO")
        val strNameTask: String? = intent.getStringExtra("TaskName")
        val strMarkTask: String? = intent.getStringExtra("Mark")
        val textGroup = "${binding.group.text} $strGroup"
        val textStudent = "${binding.student.text} $strFIO"
        val textTask = "${binding.task.text} $strNameTask"
        val textMark = "${binding.mark.text} $strMarkTask"
        binding.group.text = textGroup
        binding.student.text = textStudent
        binding.task.text = textTask
        binding.mark.text = textMark
        binding.editMark.setText(strMarkTask)
        binding.textDateChange.text = "Выберите дату сдачи, сейчас указана дата, $year-$month-$day"
        binding.dateBtnChange.setOnClickListener {
            DatePickerDialog(this,  { view, myYear, myMonth, myDayOfMonth ->
                year = myYear
                month = getMonth(myMonth.toString())
                day = getDay(myDayOfMonth.toString())
                binding.textDateChange.text = "Вы выбрали дату, $year-$month-$day"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.change.setOnClickListener{
            val dateIns = "$year-$month-$day"
            val check = update.updateMark(strIDMark,binding.editMark.text, dateIns)
            if (check) {
                Toast.makeText(applicationContext, "Данные загружены", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        }
        binding.delete.setOnClickListener{
            val check = delete.deleteMark(strIDMark)
            if (check) {
                Toast.makeText(applicationContext, "Данные удалены", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "Ошибка удаления данных", Toast.LENGTH_SHORT).show()
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