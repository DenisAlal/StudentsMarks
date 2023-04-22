package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.denisal.studentsmarks.LessonData
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.databinding.ActivityTrafficViewBinding
import com.denisal.studentsmarks.db.GetFromDB
import com.denisal.studentsmarks.scanning.CustomAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrafficViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrafficViewBinding
    private var courseArray: MutableList<SubjData> = mutableListOf()
    private var lessonArray: MutableList<LessonData> = mutableListOf()
    val db = GetFromDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrafficViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно выбрать предмет и занятие " +
                        "по которому нужно отобразить посещаемость")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        courseArray = db.getDataCourse()
        lessonArray = db.getDataLesson()
        val courseList = mutableListOf<String>()
        val childMap = mutableMapOf<String,List<String>>()
        if (courseArray.isNotEmpty() && lessonArray.isNotEmpty()) {
            for (index in courseArray.indices) {
                val courseArrayValue = courseArray[index]
                val tempList = mutableListOf<String>()
                courseList.add(courseArrayValue.name)
                for (index2 in lessonArray.indices) {
                    val value = lessonArray[index2]
                    if(value.courseId == courseArrayValue.id) {
                        if(!tempList.contains(value.name)) {
                            tempList.add(value.name)
                        }
                        childMap[courseArrayValue.name] = tempList
                    }
                }

            }
            val adapter = CustomAdapter(this,courseList,childMap)
            binding.expandableListView.setAdapter(adapter)
        } else {
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Ошибка загрузки")
                .setMessage(
                    "Данных о курсах/занятиях не было найдено"
                )
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("ОК") { _, _ ->
                finish()
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
            Toast.makeText(applicationContext, "Нет данных", Toast.LENGTH_LONG).show()
        }
        binding.expandableListView.setOnChildClickListener{ _, _, groupP, childP, _ ->
            val course = courseList[groupP]
            val lesson = childMap[course]!![childP]
            Log.e("check lesson", lesson)
            goNext(lesson)
            true
        }
    }
    private fun goNext(lesson: String) {
        val intent = Intent(this, ChooseTimeAndDateTrafficActivity::class.java)
        intent.putExtra("Lesson", lesson)
        startActivity(intent)
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
}