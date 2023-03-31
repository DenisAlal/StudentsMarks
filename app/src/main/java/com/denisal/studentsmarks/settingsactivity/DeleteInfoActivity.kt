package com.denisal.studentsmarks.settingsactivity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivityDeleteInfoBinding
import com.denisal.studentsmarks.dbfunctions.DeleteFromDb


class DeleteInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteInfoBinding
    val checkStud = -1
    val checkGradeTraff = -1
    private var deleteData: MutableList<Boolean> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Удаление данных"
        val btnAccept: Button = binding.OK
        val removeStudents: CheckBox = binding.studentsCheck
        val removeCurse :CheckBox = binding.courseDelete
        val removeLesson:CheckBox = binding.lessonDelete
        val accept: EditText = binding.acceptText
        Log.e("","$teacherID")
        removeCurse.setOnClickListener{
            removeLesson.isChecked = true

        }
        btnAccept.setOnClickListener{
            val checkAcc = accept.text.toString()
            val delete = DeleteFromDb()
            if(checkAcc == "I agree"){
                deleteData.clear()
                if (removeStudents.isChecked) {
                    deleteData.add(true)
                } else {
                    deleteData.add(false)
                }
                if (removeCurse.isChecked) {
                    deleteData.add(true)
                } else {
                    deleteData.add(false)
                }
                if (removeLesson.isChecked) {
                    deleteData.add(true)
                } else {
                    deleteData.add(false)
                }

                val check = delete.deleteInfo(deleteData)
                if (check) {
                    val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
                    val mBuilderSuccess = AlertDialog.Builder(this)
                        .setView(mDialogSuccess).setTitle("Удаление выполнено")
                    val windowSuccess = mBuilderSuccess.show()
                    object : CountDownTimer(1800, 600){
                        override fun onTick(millisUntilFinished: Long) {
                            Log.e("tik", "tak")
                        }
                        override fun onFinish() {
                            windowSuccess.dismiss()
                        }
                    }.start()
                } else {
                    val mDialogFail = LayoutInflater.from(this).inflate(R.layout.fail, null)
                    val mBuilderFail = AlertDialog.Builder(this)
                        .setView(mDialogFail).setTitle("Удаление завершилось ошибкой")
                    val windowFail = mBuilderFail.show()
                    object : CountDownTimer(1800, 600){
                        override fun onTick(millisUntilFinished: Long) {
                            Log.e("tik", "tak")
                        }
                        override fun onFinish() {
                            windowFail.dismiss()
                        }
                    }.start()
                }
            } else {
                val errorToast = Toast.makeText(
                    this@DeleteInfoActivity,
                    "Ошибка, введите подтверждение!",
                    Toast.LENGTH_SHORT
                )
                errorToast.show()
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
}