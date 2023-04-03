package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.denisal.studentsmarks.LessonData
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.databinding.ActivityCreateTaskBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.dbfunctions.InsertToDB
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateTaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var subjArray: MutableList<SubjData> = mutableListOf()
    private var getCourses: MutableList<LessonData> = mutableListOf()
    private var choiceLesson: MutableList<LessonData> = mutableListOf()
    private var choiceDate: MutableList<String> = mutableListOf()
    private val db = GetFromDB()
    private val insertToDB = InsertToDB()
    private lateinit var binding: ActivityCreateTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно выбрать создать задание")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        subjArray = db.getDataCourse()
        init()
    }

    private fun init() {
        val saveTask: Button = findViewById(R.id.saveTask)
        val spinCourse: Spinner = findViewById(R.id.choiceCourse)
        val spinLesson: Spinner = findViewById(R.id.choiceLesson)
        val spinDate: Spinner = findViewById(R.id.choiceData)
        val spinTime: Spinner = findViewById(R.id.choiceTime)
        val nameTask: TextView = findViewById(R.id.nameTask)
        saveTask.setOnClickListener {
            val position = spinCourse.selectedItemPosition
            if (nameTask.text.isNotEmpty()) {
                for (index in getCourses.indices) {
                    val value = getCourses[index]
                    if (value.courseId == subjArray[position].id
                        && value.date == spinDate.selectedItem
                        && value.time == spinTime.selectedItem
                    ) {
                        val lesCheck = spinLesson.selectedItem.toString()
                        if (lesCheck == value.name) {
                            val check = insertToDB.insertTask(
                                nameTask.text.toString(),
                                value.courseId,
                                value.id
                            )
                            if (check) {
                                val mBuilderSuccess = android.app.AlertDialog.Builder(this)
                                    .setTitle("Задание добавлено")
                                    .setMessage("Добавить еще одно задание?")
                                    .setIcon(R.drawable.baseline_check_circle_24)
                                mBuilderSuccess.setPositiveButton("Да") { _, _ ->
                                    nameTask.text = ""

                                }
                                mBuilderSuccess.setNegativeButton("Нет") { _, _ ->
                                    nameTask.text = ""
                                    finish()
                                }
                                val alertDialogError: android.app.AlertDialog = mBuilderSuccess.create()
                                alertDialogError.show()

                            }
                        }
                    }
                }

            } else {
                Toast.makeText(applicationContext, "Введите название задания", Toast.LENGTH_LONG)
                    .show()
            }

        }

        if (subjArray.isNotEmpty()) {
            val arrayForSpinnerSubj: ArrayList<String> = arrayListOf()
            for (index in subjArray.indices) {
                val value = subjArray[index]
                arrayForSpinnerSubj.add(value.name)
            }

            spinCourse.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                arrayForSpinnerSubj as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinCourse.adapter = add

        } else {
            spinCourse.isVisible = false
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if (parent.id == R.id.choiceCourse) {
            val getID = subjArray[position].id
            getData(getID)
        }
        if (parent.id == R.id.choiceLesson) {
            getDate()
        }
        if (parent.id == R.id.choiceData) {
            getTime()
        }
        if (parent.id == R.id.choiceTime) {
            val spinLesson: Spinner = findViewById(R.id.choiceLesson)
            val spinDate: Spinner = findViewById(R.id.choiceData)
            val spinTime: Spinner = findViewById(R.id.choiceTime)
            for(i in getCourses.indices) {
                val value = getCourses[i]
                if (value.name == spinLesson.selectedItem.toString() &&
                    value.date == spinDate.selectedItem.toString() &&
                    value.time == spinTime.selectedItem.toString()) {
                    binding.lessonType.setText("Тип занятия: ${value.lessonType}")
                    Log.e("aboba selectedItem" , spinLesson.selectedItem.toString())
                }
            }
        }
    }

    private fun getData(getID: Int) {
        getCourses.clear()
        getCourses = db.getLesson(getID)
        val lessonArray: MutableList<String> = arrayListOf()
        for (index in getCourses.indices) {
            val value = getCourses[index].name
            if (lessonArray.isEmpty()) {
                lessonArray.add(value)
            } else {
                if (!lessonArray.contains(value)) {
                    lessonArray.add(value)
                }
            }
        }
        val spinLesson = findViewById<Spinner>(R.id.choiceLesson)
        if (lessonArray.isNotEmpty()) {
            spinLesson.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                lessonArray as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinLesson.adapter = add
        } else {
            val builderError = AlertDialog.Builder(this)
                .setTitle("Загрузка завершилась ошибкой")
                .setMessage("Ваш список занятий для этого предмета пуст! " +
                        "Для того чтобы начать сканирование сначала создайте занятие!")
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderError.setPositiveButton("OK"){ _, _ ->
                finish()
            }
            val alertDialogError: AlertDialog = builderError.create()
            alertDialogError.show()
        }

    }

    private fun getDate() {
        val lesson: Spinner = findViewById(R.id.choiceLesson)
        val spinnerData: Spinner = findViewById(R.id.choiceData)
        choiceLesson.clear()
        choiceDate.clear()
        for (index in getCourses.indices) {
            val value = getCourses[index]
            if (value.name.contains(lesson.selectedItem.toString())) {
                choiceLesson.add(
                    LessonData(
                        value.id, value.name, value.date, value.time,
                        value.courseId, value.lessonType
                    )
                )
            }
        }
        for (index in choiceLesson.indices) {
            val value = choiceLesson[index].date
            if (!choiceDate.contains(value)) {
                choiceDate.add(value)
            }
        }
        if (choiceDate.isNotEmpty()) {
            spinnerData.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                choiceDate.toList()
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerData.adapter = add
        } else {
            spinnerData.isVisible = false
        }
    }

    private fun getTime() {
        val lesson: Spinner = findViewById(R.id.choiceLesson)
        val dateSpinner: Spinner = findViewById(R.id.choiceData)
        val timeSpinner: Spinner = findViewById(R.id.choiceTime)
        val choiceTime: MutableList<String> = mutableListOf()
        for (index in getCourses.indices) {
            val value = getCourses[index]
            if (value.name.contains(lesson.selectedItem.toString()) &&
                value.date.contains(dateSpinner.selectedItem.toString())
            ) {
                choiceTime.add(value.time)
            }
        }
        if (choiceTime.isNotEmpty()) {
            timeSpinner.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                choiceTime.toList()
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeSpinner.adapter = add
        } else {
            timeSpinner.isVisible = false
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}