package com.denisal.studentsmarks.scanning.activites

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import com.denisal.studentsmarks.R
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
        binding.goBack.setOnClickListener{
            finish()
        }
        binding.goInfo.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно изменить оценку или удалить её")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
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
                val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
                val mBuilderSuccess = AlertDialog.Builder(this)
                    .setView(mDialogSuccess).setTitle("Данные изменены")
                val windowSuccess = mBuilderSuccess.show()
                object : CountDownTimer(1200, 600){
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        windowSuccess.dismiss()
                        finish()
                    }
                }.start()
            } else {
                Toast.makeText(applicationContext, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        }
        binding.delete.setOnClickListener{
            val check = delete.deleteMark(strIDMark)
            if (check) {
                val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.fail, null)
                val mBuilderSuccess = AlertDialog.Builder(this)
                    .setView(mDialogSuccess).setTitle("Данные удалены")
                val windowSuccess = mBuilderSuccess.show()
                object : CountDownTimer(1200, 600){
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        windowSuccess.dismiss()
                        finish()
                    }
                }.start()
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