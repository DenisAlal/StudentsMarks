package com.denisal.studentsmarks.settingsactivity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityCreateCourseBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.dbfunctions.InsertToDB
import com.denisal.studentsmarks.teacherID

class CreateCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Создание предмета"
        val db = GetFromDB()
        db.getTeacher()

        binding.createSubjectBtn.setOnClickListener{
            val checkName = binding.nameSub.text.toString()
            val insert = InsertToDB()
            val checkDuplicate = db.getArraySubjDuplicate(checkName)
            if (checkName.isNotEmpty() && teacherID != -1){
                val pract =  binding.practiceCheck
                val lect = binding.lectionCheck
                if (pract.isChecked || lect.isChecked) {
                    var checkPract = 0
                    var checkLect = 0
                    if (lect.isChecked) {
                        checkLect = 1
                    }
                    if (pract.isChecked) {
                        checkPract = 1
                    }
                    if (checkDuplicate) {
                        Toast.makeText(this, "Предмет с таким названием уже существует!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("check", "$checkName $checkLect $checkPract")
                        val check = insert.insertCourse(checkName, checkLect, checkPract)
                        if(check) {
                            val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
                            val mBuilderSuccess = AlertDialog.Builder(this)
                                .setView(mDialogSuccess).setTitle("Предмет создан")
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
                            Toast.makeText(this, "Ошибка создания предмета", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(applicationContext, "Выберите тип занятий", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Введите название предмета", Toast.LENGTH_SHORT).show()
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
