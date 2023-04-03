package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivityChooseLessonTrafficBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB

class ChooseLessonTrafficActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var subjArray: MutableList<SubjData> = mutableListOf()
    private var getLesson: MutableList<LessonData> = mutableListOf()
    private var choiceLesson: MutableList<LessonData> = mutableListOf()
    private var choiceDate: MutableList<String> = mutableListOf()
    private var getIDCourse = 0
    private val db = GetFromDB()
    private lateinit var binding: ActivityChooseLessonTrafficBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLessonTrafficBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.goBack.setOnClickListener{
            finish()
        }
        binding.goInfo.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно выбрать задание по которому будет выставлена оценка")
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
        val goScan: Button = findViewById(R.id.goScanTraffic)
        val spinCourse: Spinner = findViewById(R.id.chooseCourseTraffic)
        goScan.setOnClickListener {
            if (subjArray.isNotEmpty()) {
                val lesson: Spinner = findViewById(R.id.chooseLessonTraffic)
                val dateSpinner: Spinner = findViewById(R.id.chooseDateTraffic)
                val timeSpinner: Spinner = findViewById(R.id.chooseTimeTraffic)
                for (index in getLesson.indices) {
                    val value = getLesson[index]
                    if(timeSpinner.selectedItem != null) {
                        if (value.name.contains(lesson.selectedItem.toString()) &&
                            value.date.contains(dateSpinner.selectedItem.toString()) &&
                            value.time.contains(timeSpinner.selectedItem.toString())) {
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            val intent = Intent(this, QrTrafficActivity::class.java)
                            Log.e("getID", value.id.toString())
                            intent.putExtra("LessonID", value.id)
                            startActivity(intent)
                            break
                        }
                    } else {
                        Toast.makeText(applicationContext, "Сначала необходимо выбрать время", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(applicationContext, "Сначала необходимо создать предмет", Toast.LENGTH_LONG).show()
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
        if (parent.id == R.id.chooseCourseTraffic) {
            getIDCourse = subjArray[position].id

            getCourse(getIDCourse)
        }
        if (parent.id == R.id.chooseLessonTraffic) {
            getDate()
        }
        if (parent.id == R.id.chooseDateTraffic) {
            getTime()
        }
        if (parent.id == R.id.chooseTimeTraffic) {

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
        val spinLesson = findViewById<Spinner>(R.id.chooseLessonTraffic)
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
        val lesson: Spinner = findViewById(R.id.chooseLessonTraffic)
        val spinnerData: Spinner = findViewById(R.id.chooseDateTraffic)
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
        val lesson: Spinner = findViewById(R.id.chooseLessonTraffic)
        val dateSpinner: Spinner = findViewById(R.id.chooseDateTraffic)
        val timeSpinner: Spinner = findViewById(R.id.chooseTimeTraffic)
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
}