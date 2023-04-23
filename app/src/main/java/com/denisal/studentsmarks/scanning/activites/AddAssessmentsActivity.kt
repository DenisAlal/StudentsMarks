package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityAddAssessmentsBinding
import com.denisal.studentsmarks.db.assessments.AssesDataRoom
import com.denisal.studentsmarks.db.assessments.AssessDB
import com.denisal.studentsmarks.db.students.StudentsDB
import com.denisal.studentsmarks.db.tasks.TasksDB
import com.denisal.studentsmarks.db.tasks.TasksDataRoom
import com.denisal.studentsmarks.scanning.adapters.AssessSessionAdapter
import java.util.Calendar
import kotlin.concurrent.thread


class AddAssessmentsActivity : AppCompatActivity(), AssessSessionAdapter.MyClickListener {
    private lateinit var binding: ActivityAddAssessmentsBinding
    private val cal: Calendar = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR)
    private var month = getMonth(cal.get(Calendar.MONTH).toString())
    private var day = getDay(cal.get(Calendar.DAY_OF_MONTH).toString())
    private val listTasks: MutableList<TasksDataRoom> = arrayListOf()
    private val listAsses: MutableList<AssesDataRoom> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssessmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.processLoading.isVisible = false
        binding.goBack.setOnClickListener {
            finish()
            val intent = Intent(this, SessionActivity::class.java)
            startActivity(intent)
        }
        val fioStud = intent.getStringExtra("fioStudentSession")
        val idStud = intent.getIntExtra("idStudentSession", 0)

        binding.fio.text = "ФИО: " + fioStud
        val dbTasks = TasksDB.getDB(this)
        val dbAssess = AssessDB.getDB(this)
        dbAssess.getAssesDao().getAssessByID(idStud).asLiveData().observe(this) { List ->
            listAsses.clear()
            List.forEach {
                listAsses.add(AssesDataRoom(it.id, it.value,it.taskName, it.date, it.student_id, it.task_id))

            }
            val adapter = AssessSessionAdapter(listAsses, this@AddAssessmentsActivity)
            binding.recyclerViewAssess.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewAssess.adapter = adapter
        }

        dbTasks.getTasksDao().getTasks().asLiveData().observe(this) { List ->

            val listTasksSpin: MutableList<String> = arrayListOf()

            List.forEach {
                listTasksSpin.add(it.taskName)
                listTasks.add(TasksDataRoom(it.id, it.idTask,it.taskName, it.courseName))
            }
            val add: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,
                    listTasksSpin as List<Any?>
                )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.choiceTask.adapter = add

        }
        binding.addAssess.setOnClickListener{
            val value = binding.setMark.text.toString()
            val date = "$year-$month-$day"
            thread{
                dbAssess.getAssesDao().insertAssess(AssesDataRoom(null,
                value, binding.choiceTask.selectedItem.toString(),date,
                idStud,listTasks[binding.choiceTask.selectedItemId.toInt()].idTask))
            }.join()
        }
        binding.deleteButt.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Удалить студента из списка?")
                .setMessage("Подтвердите удаление студента из успеваемости")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("Удалить"){ _, _ ->
                val dbStud = StudentsDB.getDB(this)
                thread {
                    dbStud.getStudentsDao().deleteOneStudent(idStud)
                }.join()
            }
            builderSucceed.setPositiveButton("Отмена"){ _, _ ->

            }

            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }

    }
    private fun loading(){
        binding.processLoading.isVisible = true

    }
    private fun finishLoad() {
        binding.processLoading.isVisible = false

    }

    private fun getMonth(month: String): String {
        val newMon = month.toInt() + 1
        var newMonth = newMon.toString()
        if (month.length < 2) {
            newMonth = "0$newMonth"
        }
        return newMonth
    }

    private fun getDay(day: String): String {
        val newd = day.toInt()
        var newDay = newd.toString()
        if (day.length < 2) {
            newDay = "0$newDay"
        }
        return newDay
    }


    override fun onClick(position: Int) {
        val idStud = intent.getIntExtra("idStudentSession", 0)
        val dbAsses = AssessDB.getDB(this)
        Log.e("Dwajkhnfkjwa    id", idStud.toString())
        val builderSucceed = AlertDialog.Builder(this)
            .setTitle("Удалить оценку из списка?")
            .setMessage("Подтвердите удаление оценки")
            .setIcon(R.drawable.baseline_error_outline_24_orange)
        builderSucceed.setNegativeButton("Удалить"){ _, _ ->
            thread{ listAsses[position].id?.let { dbAsses.getAssesDao().deleteOneAssess(it) } }.join()
            val dbAssess = AssessDB.getDB(this)
            dbAssess.getAssesDao().getAssessByID(idStud).asLiveData().observe(this) { List ->
                listAsses.clear()
                List.forEach {
                    listAsses.add(AssesDataRoom(it.id, it.value,it.taskName, it.date, it.student_id, it.task_id))

                }
                val adapter = AssessSessionAdapter(listAsses, this@AddAssessmentsActivity)
                binding.recyclerViewAssess.layoutManager = LinearLayoutManager(this)
                binding.recyclerViewAssess.adapter = adapter
            }
        }
        builderSucceed.setPositiveButton("Отмена"){ _, _ ->

        }

        val alertDialogSuccess: AlertDialog = builderSucceed.create()
        alertDialogSuccess.show()

    }
}