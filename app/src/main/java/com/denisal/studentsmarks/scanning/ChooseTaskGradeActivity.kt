package com.denisal.studentsmarks.scanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivityChooseTaskGradeBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB

class ChooseTaskGradeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var subjArray: MutableList<SubjData> = mutableListOf()
    private var getLesson: MutableList<LessonData> = mutableListOf()
    private var choiceLesson: MutableList<LessonData> = mutableListOf()
    private var choiceDate: MutableList<String> = mutableListOf()
    private var tasks: MutableList<TaskData> = mutableListOf()
    private var getIDCourse = 0
    private val db = GetFromDB()
    private lateinit var binding: ActivityChooseTaskGradeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseTaskGradeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Выбор задания"
        subjArray = db.getDataCourse()
        init()
    }
    private fun init() {
        val goScan: Button = findViewById(R.id.goScan)
        val spinCourse: Spinner = findViewById(R.id.chooseCourse)
        val spintask: Spinner = findViewById(R.id.chooseTask)
        goScan.setOnClickListener {
            if(spintask.selectedItem.toString() != "Не было найдено заданий") {
                if (tasks.isNotEmpty()) {
                    val position = spintask.selectedItemPosition
                    id_task = tasks[position].id
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    val intent = Intent(this, QrGradeActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(applicationContext, "Сначала необходимо создать задание", Toast.LENGTH_LONG).show()
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
        if (parent.id == R.id.chooseCourse) {
            getIDCourse = subjArray[position].id
            Log.e("idCourse", getIDCourse.toString())
            getCourse(getIDCourse)
        }
        if (parent.id == R.id.chooseLesson) {
            getDate()
        }
        if (parent.id == R.id.chooseDate) {
            getTime()
        }
        if (parent.id == R.id.chooseTime) {
            getDataTask()
        }
        if (parent.id == R.id.chooseTask) {

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun getCourse(getID: Int) {
        getLesson.clear()
        getLesson = db.getLesson(getID)
        val lessonArray: MutableList<String> = arrayListOf()
        for (index in getLesson.indices) {
            val value = getLesson[index].name
            if (lessonArray.isEmpty()) {
                lessonArray.add(value)
            } else {
                if (!lessonArray.contains(value)) {
                    lessonArray.add(value)
                }
            }
        }
        val spinLesson = findViewById<Spinner>(R.id.chooseLesson)
        if (lessonArray.isNotEmpty()) {
            spinLesson.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                lessonArray as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinLesson.adapter = add
        } else {

        }
    }

    private fun getDate() {
        val lesson: Spinner = findViewById(R.id.chooseLesson)
        val spinnerData: Spinner = findViewById(R.id.chooseDate)
        choiceLesson.clear()
        choiceDate.clear()
        for (index in getLesson.indices) {
            val value = getLesson[index]
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
        val lesson: Spinner = findViewById(R.id.chooseLesson)
        val dateSpinner: Spinner = findViewById(R.id.chooseDate)
        val timeSpinner: Spinner = findViewById(R.id.chooseTime)
        val choiceTime: MutableList<String> = mutableListOf()
        for (index in getLesson.indices) {
            val value = getLesson[index]
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

    private fun getDataTask() {
        val lesson: Spinner = findViewById(R.id.chooseLesson)
        val dateSpinner: Spinner = findViewById(R.id.chooseDate)
        val timeSpinner: Spinner = findViewById(R.id.chooseTime)
        val taskSpinner: Spinner = findViewById(R.id.chooseTask)
        val getLesson: MutableList<LessonData> = db.getLessonForTask(
            getIDCourse,
            lesson.selectedItem.toString(),
            dateSpinner.selectedItem.toString(),
            timeSpinner.selectedItem.toString()
        )
        if (getLesson.isNotEmpty()) {
            tasks= db.getTasks(getIDCourse, getLesson[0].id)
            val namesTasks: MutableList<String> = arrayListOf()
            if (tasks.isNotEmpty()) {
                for (index in tasks.indices) {
                    val value = tasks[index].name
                    namesTasks.add(value)
                }
                taskSpinner.onItemSelectedListener = this
                val add: ArrayAdapter<*> = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item,
                    namesTasks
                )
                add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                taskSpinner.adapter = add
            } else {
                val nullTasks = arrayListOf("Не было найдено заданий")
                taskSpinner.onItemSelectedListener = this
                val add: ArrayAdapter<*> = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item,
                    nullTasks
                )
                add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                taskSpinner.adapter = add
            }
        } else {

        }

    }
}