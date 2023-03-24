package com.denisal.studentsmarks.scanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityGradeViewBinding
import com.denisal.studentsmarks.databinding.ActivitySetTaskAndFioactivityBinding
import kotlin.random.Random

class SetTaskAndFIOActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySetTaskAndFioactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetTaskAndFioactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"
        val strCourse: String? = intent.getStringExtra("course")
        val strLesson: String? = intent.getStringExtra("lesson")
        binding.course.text = strCourse
        binding.lesson.text = strLesson
        val groupList = mutableListOf<String>()
        val childMap = mutableMapOf<String,List<String>>()
        repeat(15){groupId->
            groupList.add("Group $groupId")
            val childNumber = Random.nextInt(10)
            val tempList = mutableListOf<String>()
            repeat(childNumber){childId->
                tempList.add("Child id = ${childId}")
            }
            childMap["Group $groupId"] = tempList
        }

        val adapter = CustomAdapter(this,groupList,childMap)
        binding.expandableListViewTasks.setAdapter(adapter)

        binding.expandableListViewTasks.setOnChildClickListener{ _, _, groupP, childP, _ ->
            val course = groupList[groupP]
            val lesson = childMap[course]!![childP]
            Toast.makeText(this,"$course  $lesson", Toast.LENGTH_SHORT).show()
            true
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
}