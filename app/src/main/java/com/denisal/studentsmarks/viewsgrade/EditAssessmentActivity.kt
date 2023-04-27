package com.denisal.studentsmarks.viewsgrade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityEditAssessmentBinding
import com.denisal.studentsmarks.databinding.ActivityGradeViewBinding
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class EditAssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAssessmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAssessmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val split = intent.getStringExtra("arrayEdit")
        val splitted = split?.split("|")
        if (splitted != null) {
            for (i in splitted.indices) {
                Log.e("dwada", splitted[i])
            }
        }
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            val intent = Intent(this, GradeViewActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.valueChangeText.setText(splitted?.get(1))
        binding.course.text = "Предмет: ${splitted?.get(6)}"
        binding.lesson.text = "Занятие: ${splitted?.get(5)}"
        binding.task.text = "Задание: ${splitted?.get(4)}"
        binding.fio.text = "ФИО: ${splitted?.get(2)}"
        binding.group.text = "Группа: ${splitted?.get(3)}"

        binding.change.setOnClickListener{
            val change = binding.valueChangeText.text.toString()
            val value = splitted?.get(1)
            if (change != value) {
                Thread {
                    try {
                        Class.forName("com.mysql.jdbc.Driver")
                        val cn: Connection = DriverManager.getConnection(url, user, pass)
                        val ps = cn.createStatement()
                        val insert = "UPDATE assessment SET value = '$change' WHERE id = ${splitted?.get(0)}"
                        ps.execute(insert)
                        ps?.close()
                        cn.close()
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                    runOnUiThread{
                        val intent = Intent(this, GradeViewActivity::class.java)
                        startActivity(intent)
                        finish()}
                }.start()
            } else {
                Toast.makeText(applicationContext, "Данное значение уже записано", Toast.LENGTH_SHORT).show()
            }
        }
        binding.deleteAssess.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Удалить оценку?")
                .setMessage("Подтвердите удаление оценки студента")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("Удалить"){ _, _ ->

                Thread {
                    try {
                        Class.forName("com.mysql.jdbc.Driver")
                        val cn: Connection = DriverManager.getConnection(url, user, pass)
                        val ps = cn.createStatement()
                        val insert = "DELETE FROM assessment WHERE id = ${splitted?.get(0)}"
                        ps.execute(insert)
                        ps?.close()
                        cn.close()
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                    runOnUiThread{
                        val intent = Intent(this, GradeViewActivity::class.java)
                        startActivity(intent)
                        finish()}
                }.start()

            }
            builderSucceed.setPositiveButton("Отмена"){ _, _ ->

            }

            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }

    }
}