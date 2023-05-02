package com.denisal.studentsmarks.settingsactivity.homefragments

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class SaveDataActivity : AppCompatActivity() {
    private var tableList: MutableList<MutableList<String>> = arrayListOf()
    private val SAVE_REQUEST_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_data)
        val goBack: FloatingActionButton = findViewById(R.id.goBack)
        goBack.setOnClickListener{
            finish()
        }
        val saveGrade: Button = findViewById(R.id.saveGrade)
        saveGrade.setOnClickListener {
            val table = "Успеваемость"
            val sql =
                "SELECT assessment.id, course.name, lesson.name, task.name, lesson.numberSubj, lesson.lessonType, student.studGroup, student.fullName, assessment.date, assessment.value " +
                        " FROM assessment INNER JOIN student ON student_id = student.id " +
                        "INNER JOIN task ON task_id = task.id INNER JOIN lesson ON lesson_id = lesson.id " +
                        "INNER JOIN course ON task.course_id = course.id WHERE student.teacher_id = $teacherID ORDER BY course.name ASC, lesson.name ASC"
            val typeDataGetting = "grade"
            getDataFromDB(sql, table, typeDataGetting)

        }
        val saveTraffic: Button = findViewById(R.id.saveTraffic)
        saveTraffic.setOnClickListener {
            val table = "Посещаемость"
            val sql =
                "SELECT course.name, lesson.name, lesson.numberSubj, lesson.lessonType, lesson.date, student.studGroup, student.fullName " +
                        " FROM traffic INNER JOIN student ON student_id = student.id INNER JOIN lesson ON traffic.lesson_id = lesson.id " +
                        "INNER JOIN course ON lesson.course_id = course.id WHERE student.teacher_id = $teacherID ORDER BY course.name ASC, lesson.name ASC"
            val typeDataGetting = "traffic"
            getDataFromDB(sql, table, typeDataGetting)

        }
    }

    private fun getDataFromDB(sqlSelect: String, table: String, typeDataGetting: String) {
        tableList.clear()
        if (typeDataGetting == "grade") {
            if (tableList.isEmpty()) {
                tableList.add(
                    arrayListOf(
                        "Предмет",
                        "Занятие",
                        "Задание",
                        "Номер пары",
                        "Тип занятия",
                        "Группа",
                        "ФИО",
                        "Дата выставления",
                        "Оценка"
                    )
                )
                Thread {
                    try {
                        Class.forName("com.mysql.jdbc.Driver")
                        val cn: Connection = DriverManager.getConnection(url, user, pass)
                        val query = cn.prepareStatement(sqlSelect)
                        val result = query.executeQuery()
                        while (result.next()) {
                            val courseName = result.getString(2)
                            val lessonName = result.getString(3)
                            val taskName = result.getString(4)
                            val numberSubj = result.getString(5)
                            val lessonType = result.getString(6)
                            val group = result.getString(7)
                            val fio = result.getString(8)
                            val date = result.getString(9)
                            val value = result.getString(10)
                            tableList.add(
                                arrayListOf(
                                    courseName,
                                    lessonName,
                                    taskName,
                                    numberSubj,
                                    lessonType,
                                    group,
                                    fio,
                                    date,
                                    value
                                )
                            )
                        }
                        cn.close()

                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                    runOnUiThread {
                        saveToCsv(table)
                    }
                }.start()
            }
        }
        if (typeDataGetting == "traffic") {
            if (tableList.isEmpty()) {
                tableList.add(
                    arrayListOf(
                        "Предмет",
                        "Занятие",
                        "Номер пары",
                        "Тип занятия",
                        "Дата занятия",
                        "Группа",
                        "ФИО"
                    )
                )
                Thread {
                    try {
                        Class.forName("com.mysql.jdbc.Driver")
                        val cn: Connection = DriverManager.getConnection(url, user, pass)
                        val query = cn.prepareStatement(sqlSelect)
                        val result = query.executeQuery()
                        while (result.next()) {
                            val courseName = result.getString(1)
                            val lessonName = result.getString(2)
                            val numberSubj = result.getString(3)
                            val typeSubj = result.getString(4)
                            val date = result.getString(5)
                            val group = result.getString(6)
                            val fio = result.getString(7)
                            tableList.add(
                                arrayListOf(
                                    courseName,
                                    lessonName,
                                    numberSubj,
                                    typeSubj,
                                    date,
                                    group,
                                    fio
                                )
                            )
                        }
                        cn.close()

                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                    runOnUiThread {
                        saveToCsv(table)
                    }
                }.start()
            }
        }

    }

    private fun saveToCsv(table: String) {
        val fileIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "$table.csv")
        }
        startActivityForResult(fileIntent, SAVE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == SAVE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    BufferedWriter(OutputStreamWriter(outputStream, Charsets.UTF_8)).use { writer ->
                        for (row in tableList) {
                            writer.write(row.joinToString(","))
                            writer.newLine()
                        }
                    }
                    Toast.makeText(this, "Данные сохраненены!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
