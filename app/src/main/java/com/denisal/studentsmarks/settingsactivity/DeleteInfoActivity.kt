package com.denisal.studentsmarks.settingsactivity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivityDeleteInfoBinding

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.concurrent.TimeUnit


class DeleteInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteInfoBinding
    val checkStud = -1
    val checkGradeTraff = -1
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
        val removeGrade: CheckBox = binding.gradeDelete
        val accept: EditText = binding.acceptText
        Log.e("","$teacherID")
        removeStudents.setOnClickListener{
            binding.gradeDelete.isChecked = true
        }
        removeCurse.setOnClickListener{
            removeLesson.isChecked = true
            binding.gradeDelete.isChecked = true
        }
        removeLesson.setOnClickListener{
            binding.gradeDelete.isChecked = true
        }
        btnAccept.setOnClickListener{
            val checkAcc = accept.text.toString()
            if(checkAcc == "I agree"){
                if (removeStudents.isChecked) {
                    Thread(Runnable {
                        deleteStudList()
                    }).start()
                }
                if (removeGrade.isChecked) {
                    Thread(Runnable {
                        deleteGrade()
                    }).start()
                }
                if (removeLesson.isChecked) {
                    Thread(Runnable {
                        deleteLesson()
                    }).start()
                }
                if (removeCurse.isChecked) {
                    Thread(Runnable {
                        deleteCourse()
                    }).start()
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
    private fun deleteStudList() {
        TimeUnit.SECONDS.sleep(1)
        runOnUiThread {startDelete()}
        if (teacherID != -1) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val ps = cn.createStatement()
                val insert = "DELETE FROM student WHERE teacher_id='$teacherID'"
                ps.execute(insert)
                ps?.close()
                cn.close()
            } catch (c: ClassNotFoundException) {
                c.printStackTrace()
            } catch (c: SQLException) {
                c.printStackTrace()
            }
            runOnUiThread { deleted()}
        }
    }

    private fun deleteGrade() {
        Log.e("delete DB", "delete DB")
    }
    private fun deleteCourse() {
        TODO("Not yet implemented")
    }

    private fun deleteLesson() {
        TODO("Not yet implemented")
    }
    private fun startDelete(){
    //действие при начале удаления
    }
    private fun deleted(){
       // действие при конце
    }
}