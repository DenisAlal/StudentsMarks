package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.LessonData
import com.denisal.studentsmarks.databinding.ActivityChooseTimeAndDateTrafficBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.scanning.CustomAdapter


class ChooseTimeAndDateTrafficActivity : AppCompatActivity() {
    private var array: MutableList<LessonData> = mutableListOf()
    private lateinit var binding: ActivityChooseTimeAndDateTrafficBinding
    private var strLesson: String?  = ""
    val db = GetFromDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseTimeAndDateTrafficBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"
        strLesson = intent.getStringExtra("Lesson")
        array = db.getLessonDateTime(strLesson.toString())
        Log.e("Check lesson2", strLesson.toString())
        Log.e("checkArray", array.toString())
        val courseList = mutableListOf<String>()
        val childMap = mutableMapOf<String,List<String>>()
        if (array.isNotEmpty()) {
            for (index in array.indices) {
                val value1 = array[index]
                val tempList = mutableListOf<String>()
                if (!courseList.contains(value1.date)) {
                    courseList.add(value1.date)
                }
                for (index2 in array.indices) {
                    val value2 = array[index2]
                    if (value1.date == value2.date) {
                        tempList.add("${value2.lessonType}:  ${value2.time}")
                        childMap[value1.date] = tempList
                    }
                }

            }
            val adapter = CustomAdapter(this,courseList,childMap)
            binding.expandableListViewDateTime.setAdapter(adapter)
        } else {
            Toast.makeText(applicationContext, "Нет данных", Toast.LENGTH_LONG).show()
        }
        binding.expandableListViewDateTime.setOnChildClickListener{ _, _, groupP, childP, _ ->
            val date = courseList[groupP]
            val time = childMap[date]!![childP]
            val timeSplit: List<String> = time.split(":  ")
            for(i in array.indices) {
                val value = array[i]
                if(value.name == strLesson && date == value.date && timeSplit[1] == value.time) {
                    goNext(value.id)
                }
            }

            true
        }
    }
    private fun goNext(idLesson: Int) {
        val intent = Intent(this, TrafficViewStudentsActivity::class.java)
        intent.putExtra("IDLesson", idLesson)
        Log.e("check idLesson",  idLesson.toString())
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