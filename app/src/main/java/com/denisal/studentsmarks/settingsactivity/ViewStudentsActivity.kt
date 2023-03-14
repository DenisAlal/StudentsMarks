package com.denisal.studentsmarks.settingsactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.collections.ArrayList

class ViewStudentsActivity : AppCompatActivity() {
    private val data = ArrayList<StudentsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_student)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        visibleSetup()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Список студентов"
        Thread {
           getStudents()
        }.start()
        val adapter = StudentsAdapter(data)
        recyclerView.adapter = adapter

    }
    private fun visibleSetup(){
        val iconEmpty  = findViewById<ImageView>(R.id.iconEmpty)
        val textEmpty  = findViewById<TextView>(R.id.textEmpty)
        iconEmpty.isVisible = false
        textEmpty.isVisible = false
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
        val iconEmpty  = findViewById<ImageView>(R.id.iconEmpty)
        val textEmpty  = findViewById<TextView>(R.id.textEmpty)
        val processLoading: ProgressBar = findViewById(R.id.processLoading)
        val listView: RecyclerView = findViewById(R.id.recyclerView)
        processLoading.isVisible = false
        listView.isVisible = true
        if (data.isEmpty()) {
            textEmpty.isVisible = true
            iconEmpty.isVisible = true
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
                    val student = StudentsViewModel(id = id, fio = fio, group = group)
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
}