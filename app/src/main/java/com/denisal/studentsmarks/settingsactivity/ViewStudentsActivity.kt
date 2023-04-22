package com.denisal.studentsmarks.settingsactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import kotlin.collections.ArrayList

class ViewStudentsActivity : AppCompatActivity(), StudentsAdapter.MyClickListener {
    private val data = ArrayList<ViewModelStudents>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_student)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        Thread {
           getStudents()
        }.start()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = StudentsAdapter(data, this@ViewStudentsActivity)
        recyclerView.adapter = adapter
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("Для удаления студента, нажмите на окошко нужного студента")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
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

    private fun loading(){
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
                .setMessage("Внимание! Список студентов пуст," +
                        " для отображения списка студентов необходимо их добавить!")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("ОК"){ _, _ ->
                finish()
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
    }
    private fun getStudents() {

        runOnUiThread {
            loading()
        }
        if (teacherID != -1) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts = "SELECT * FROM student WHERE teacher_id = '${teacherID}'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val uid = result.getString(2)
                    val group = result.getString(3)
                    val fio = result.getString(4)
                    Log.e("select    ", "$id-$uid-$group-$fio")
                    val student = ViewModelStudents(id = id, fio = fio, group = group)
                    data.add(student)
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
            .setTitle("Удалить студента из списка?")
            .setMessage("Внимание! Удаление студента произведет удаление его успеваемости и посещаемости из системы!")
            .setIcon(R.drawable.baseline_error_outline_24_orange)
        builderSucceed.setNegativeButton("Удалить"){ _, _ ->
            val recycler: RecyclerView = findViewById(R.id.recyclerView)
            recycler.removeAllViewsInLayout();
            val delete = DeleteFromDb()
            delete.deleteOneStudent(data[position].id)
            data.clear()
            Thread {
                getStudents()
            }.start()
            recycler.layoutManager = LinearLayoutManager(this)
            val adapter = StudentsAdapter(data, this@ViewStudentsActivity)
            recycler.adapter = adapter
        }
        builderSucceed.setPositiveButton("Отмена"){ _, _ ->

        }

        val alertDialogSuccess: AlertDialog = builderSucceed.create()
        alertDialogSuccess.show()
    }
}