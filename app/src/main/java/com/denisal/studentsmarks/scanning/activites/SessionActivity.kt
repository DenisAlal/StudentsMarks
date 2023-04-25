package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.HomeActivity
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivitySessionBinding
import com.denisal.studentsmarks.db.assessments.AssesDataRoom
import com.denisal.studentsmarks.db.assessments.AssessDB
import com.denisal.studentsmarks.db.session.SessionDB
import com.denisal.studentsmarks.db.session.SessionData
import com.denisal.studentsmarks.db.students.StudentsDB
import com.denisal.studentsmarks.db.students.StudentsDataRoom
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

class SessionActivity : AppCompatActivity(), StudentsSessionAdapter.MyClickListener {
    private lateinit var binding: ActivitySessionBinding
    private val array: MutableList<ViewModelStudentsSession> = arrayListOf()
    private var data: MutableList<TasksDataRoom> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        val dbStudents = StudentsDB.getDB(this)
        val dbSess = SessionDB.getDB(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        binding.buttomButtons.isVisible = false
        binding.recyclerViewStudentsSession.isVisible = false
        binding.processLoading.isVisible = false
        setContentView(binding.root)
        var lesson: MutableList<SessionData> = arrayListOf()
        Thread {
            runOnUiThread { loading() }
            getTasks()
            lesson = dbSess.getSessionDao().loadOneLesson()
            runOnUiThread { finishLoad() }
        }.start()
        binding.goBack.setOnClickListener {
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
        binding.closeSession.setOnClickListener {
            if (array.isNotEmpty()) {
                closeSession(
                    lesson[0].nameLesson,
                    lesson[0].date,
                    lesson[0].lessonType,
                    lesson[0].idCourse,
                    lesson[0].numberSubj
                )
            } else {
                Toast.makeText(applicationContext, "Список посещаемости пуст! Нет данных для сохранения!", Toast.LENGTH_LONG).show()
                deleteInternalDB()
            }
        }
    }

    private fun closeSession(
        nameLesson: String,
        date: String,
        lessonType: String,
        idCourse: Int,
        numberSubj: Int
    ) {
        val builderSucceed = AlertDialog.Builder(this)
            .setTitle("Сохранить успеваемость?")
            .setMessage("Подтвердите сохранение")
            .setIcon(R.drawable.baseline_error_outline_24_orange)
        builderSucceed.setNegativeButton("Сохранить") { _, _ ->
            Thread {
                runOnUiThread { inserting() }
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert =
                        "INSERT INTO lesson (`id`, `name`, `teacher_id`, `date`, `numberSubj`, `course_id`, `lessonType`) VALUES" +
                                "(NULL, '$nameLesson', '$teacherID', '$date', $numberSubj, $idCourse, '$lessonType');"
                    ps.execute(insert)
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                runOnUiThread{getLesson(nameLesson, date, numberSubj, idCourse)}
            }.start()
        }
        builderSucceed.setPositiveButton("Отмена") { _, _ ->

        }
        val alertDialogSuccess: AlertDialog = builderSucceed.create()
        alertDialogSuccess.show()
    }

    private fun getLesson(nameLesson: String, date: String, numberSubj: Int, idCourse: Int) {
        Log.e("id lesson", "aboba")
        var id: Int? = null
        Thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts =
                    "SELECT id FROM lesson WHERE teacher_id = '$teacherID' AND name = '$nameLesson' AND date = '$date' AND course_id = '$idCourse' AND numberSubj = '$numberSubj'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    id = result.getInt(1)
                    break
                }
                Log.e("id lesson", id.toString())
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            runOnUiThread { insertTraffic(id)}
        }.start()

    }
    private fun insertTraffic(lessonId: Int?) {
        val dbStudents = StudentsDB.getDB(this)
        Thread {
            val students = dbStudents.getStudentsDao().loadAllStudentsArray()
            // traffic
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val ps = cn.createStatement()
                for (i in students.indices) {
                    val value = students[i]
                    val insert =
                        "INSERT INTO traffic (`id`, `student_id`, `lesson_id`) VALUES " +
                                "(NULL, '${value.idStud}', '$lessonId');"
                    ps.execute(insert)
                }
                ps?.close()
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

            runOnUiThread{insertAssessment(lessonId)}
        }.start()
    }
    private fun insertAssessment(lessonId: Int?) {
        val dbAssess = AssessDB.getDB(this)
        Thread{
           val assessment = dbAssess.getAssesDao().loadAllAssess()
            if (assessment.isNotEmpty()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    for (i in assessment.indices) {
                        val value = assessment[i]
                        val insert =
                            "INSERT INTO assessment (`id`, `value`, `date`, `student_id`, `task_id`, `lesson_id`) VALUES" +
                                    "(NULL, '${value.value}', '${value.date}', '${value.student_id}', '${value.task_id}', '$lessonId');"
                        ps.execute(insert)
                    }
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(applicationContext, "Нет успеваемости у студентов, выгружена только успеваемость", Toast.LENGTH_LONG).show()
            }
            runOnUiThread{deleteInternalDB()}
        }.start()
    }
    private fun deleteInternalDB() {
        val dbSession = SessionDB.getDB(this)
        val dbStudents = StudentsDB.getDB(this)
        val dbTasks = TasksDB.getDB(this)
        val dbAssess = AssessDB.getDB(this)
        Thread {
            dbSession.getSessionDao().deleteSession()
            dbSession.getSessionDao().resetAutoIncrementValueSession()
            dbStudents.getStudentsDao().deleteStudents()
            dbStudents.getStudentsDao().resetAutoIncrementValueStudents()
            dbTasks.getTasksDao().deleteTasks()
            dbTasks.getTasksDao().resetAutoIncrementValueTasks()
            dbAssess.getAssesDao().deleteAssess()
            dbAssess.getAssesDao().resetAutoIncrementValueAssess()
            runOnUiThread { goHome() }
        }.start()
    }

    private fun goHome() {
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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

    override fun onResume() {
        super.onResume()
        val dbStudents = StudentsDB.getDB(this)
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

    private fun loading() {
        binding.processLoading.isVisible = true

    }

    private fun inserting() {
        binding.processLoading.isVisible = true
        binding.buttomButtons.isVisible = false
        binding.recyclerViewStudentsSession.isVisible = false
    }

    private fun finishLoad() {
        binding.processLoading.isVisible = false
        binding.buttomButtons.isVisible = true
        binding.recyclerViewStudentsSession.isVisible = true
    }

}