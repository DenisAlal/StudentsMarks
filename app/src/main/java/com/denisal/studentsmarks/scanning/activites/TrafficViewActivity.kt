package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.denisal.studentsmarks.LessonData
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.databinding.ActivityTrafficViewBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.scanning.CustomAdapter

class TrafficViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrafficViewBinding
    private var courseArray: MutableList<SubjData> = mutableListOf()
    private var lessonArray: MutableList<LessonData> = mutableListOf()
    val db = GetFromDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrafficViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"
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