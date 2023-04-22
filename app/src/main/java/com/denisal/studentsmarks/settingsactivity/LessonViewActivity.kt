package com.denisal.studentsmarks.settingsactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.db.DeleteFromDb
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class LessonViewActivity : AppCompatActivity(), LessonAdapter.MyClickListener {
    private val data = ArrayList<ViewModelLessons>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("Для удаления занятия, нажмите на окошко нужного предмета")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        Thread {
            getLessons()
        }.start()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = LessonAdapter(data, this@LessonViewActivity)
        recyclerView.adapter = adapter

    }


    private fun loading() {
        val processLoading: ProgressBar = findViewById(R.id.processLoading)
        val listView: RecyclerView = findViewById(R.id.recyclerView)
        processLoading.isVisible = true
        listView.isVisible = false
    }

    private fun finishLoad() {

        val processLoading: ProgressBar = findViewById(R.id.processLoading)
        val listView: RecyclerView = findViewById(R.id.recyclerView)
        processLoading.isVisible = false
        listView.isVisible = true
        if (data.isEmpty()) {
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Ошибка загрузки")
                .setMessage(
                    "Внимание! Список занятий пуст," +
                            " для отображения списка занятий необходимо их добавить!"
                )
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("ОК") { _, _ ->
                finish()
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
    }

    private fun getLessons() {

        runOnUiThread {
            loading()
        }
        if (teacherID != -1) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts = "SELECT * FROM lesson WHERE teacher_id = '$teacherID'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val date = result.getString(4)
                    val time = result.getString(5)
                    val lessonType = result.getString(7)
                    data.add(ViewModelLessons(id, name, date, time, lessonType))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            runOnUiThread {
                finishLoad()
            }

        }
    }

    override fun onClick(position: Int) {

        val builderSucceed = AlertDialog.Builder(this)
            .setTitle("Удалить занятие из списка?")
            .setMessage(
                "Внимание! Удаление предмета произведет удаление данные о успеваемости " +
                        "и посещаемости из системы по этому предмету!"
            )
            .setIcon(R.drawable.baseline_error_outline_24_orange)
        builderSucceed.setNegativeButton("Удалить") { _, _ ->
            val recycler: RecyclerView = findViewById(R.id.recyclerView)
            recycler.removeAllViewsInLayout();
            val delete = DeleteFromDb()
            delete.deleteOneLesson(data[position].id)
            data.clear()
            Thread {
                getLessons()
            }.start()
            recycler.layoutManager = LinearLayoutManager(this)
            val adapter = LessonAdapter(data, this@LessonViewActivity)
            recycler.adapter = adapter
        }
        builderSucceed.setPositiveButton("Отмена") { _, _ ->

        }

        val alertDialogSuccess: AlertDialog = builderSucceed.create()
        alertDialogSuccess.show()
    }
}