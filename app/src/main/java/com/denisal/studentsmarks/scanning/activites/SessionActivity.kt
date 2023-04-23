package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.HomeActivity
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivitySessionBinding
import com.denisal.studentsmarks.db.assessments.AssessDB
import com.denisal.studentsmarks.db.session.SessionDB
import com.denisal.studentsmarks.db.students.StudentsDB
import com.denisal.studentsmarks.db.tasks.TasksDB
import com.denisal.studentsmarks.db.tasks.TasksDataRoom
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.scanning.adapters.StudentsSessionAdapter
import com.denisal.studentsmarks.scanning.viewmodels.ViewModelStudentsSession
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class SessionActivity : AppCompatActivity(), StudentsSessionAdapter.MyClickListener {
    private lateinit var binding: ActivitySessionBinding
    private val array: MutableList<ViewModelStudentsSession> = arrayListOf()
    private var data: MutableList<TasksDataRoom> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        val dbSession = SessionDB.getDB(this)
        val dbStudents = StudentsDB.getDB(this)
        val dbTasks = TasksDB.getDB(this)
        val dbAssess = AssessDB.getDB(this)

        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        binding.buttomButtons.isVisible = false
        binding.recyclerViewStudentsSession.isVisible = false
        binding.processLoading.isVisible = false
        setContentView(binding.root)
        Thread {  runOnUiThread { loading() }
            getTasks()
            runOnUiThread { finishLoad() }}.start()
        binding.goBack.setOnClickListener {
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.closeSession.setOnClickListener {
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Сохранить успеваемость?")
                .setMessage("Подтвердите сохранение")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("Удалить"){ _, _ ->
                thread {
                    dbSession.getSessionDao().deleteSession()
                    dbSession.getSessionDao().resetAutoIncrementValueSession()
                    dbStudents.getStudentsDao().deleteStudents()
                    dbStudents.getStudentsDao().resetAutoIncrementValueStudents()
                    dbTasks.getTasksDao().deleteTasks()
                    dbTasks.getTasksDao().resetAutoIncrementValueTasks()
                    dbAssess.getAssesDao().deleteAssess()
                    dbAssess.getAssesDao().resetAutoIncrementValueAssess()
                }.join()
                finish()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            builderSucceed.setPositiveButton("Отмена"){ _, _ ->

            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()

        }
        binding.scanQR.setOnClickListener {
            finish()
            val intent = Intent(this, QrTrafficActivity::class.java)
            startActivity(intent)
        }

        dbStudents.getStudentsDao().getStudents().asLiveData().observe(this) { List ->
            array.clear()
            List.forEach {
                array.add(ViewModelStudentsSession(it.idStud, it.fio, it.group))
            }
            val adapter = StudentsSessionAdapter(array, this@SessionActivity)
            binding.recyclerViewStudentsSession.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewStudentsSession.adapter = adapter
        }
    }

    private fun getTasks() {
        if (teacherID != -1) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts =
                    "SELECT * FROM task INNER JOIN course ON course_id = course.id WHERE teacher_id = '$teacherID'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    val idTask = result.getInt(1)
                    val taskName = result.getString(2)
                    val courseName = result.getString(5)
                    data.add(TasksDataRoom(null, idTask, taskName, courseName))

                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(position: Int) {
        val dbTasks = TasksDB.getDB(this)
        Thread {
            dbTasks.getTasksDao().deleteTasks()
            for (i in data.indices) {
                val value = data[i]
                val item = TasksDataRoom(value.id, value.idTask, value.taskName, value.courseName)
                dbTasks.getTasksDao().insertTasks(item)
            }
        }.start()

        val intent = Intent(this, AddAssessmentsActivity::class.java)
        intent.putExtra("idStudentSession", array[position].id)
        intent.putExtra("fioStudentSession", array[position].fio)
        startActivity(intent)
    }

    private fun loading(){
        binding.processLoading.isVisible = true

    }
    private fun finishLoad() {
        binding.processLoading.isVisible = false
        binding.buttomButtons.isVisible = true
        binding.recyclerViewStudentsSession.isVisible = true
    }

}