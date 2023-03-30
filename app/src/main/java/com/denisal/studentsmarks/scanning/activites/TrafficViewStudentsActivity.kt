package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.databinding.ActivityTrafficViewStudentsBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.scanning.CustomAdapter


class TrafficViewStudentsActivity : AppCompatActivity() {
    val db = GetFromDB()
    private lateinit var binding: ActivityTrafficViewStudentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrafficViewStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"
        val idLessonTraffic: Int = intent.getIntExtra("IDLesson", -1)
        binding.emptyStudents.isVisible = false
        val courseList = mutableListOf<String>()
        val childMap = mutableMapOf<String,List<String>>()
        if(idLessonTraffic != -1) {
            val studentsTraffic = db.getTraffic(idLessonTraffic)
            if (studentsTraffic.isNotEmpty()) {
                for (index in studentsTraffic.indices) {
                    val value1 = studentsTraffic[index]
                    val tempList = mutableListOf<String>()
                    if (!courseList.contains(value1.group)) {
                        courseList.add(value1.group)
                    }
                    for (index2 in studentsTraffic.indices) {
                        val value2 = studentsTraffic[index2]
                        if (value1.group == value2.group) {
                            tempList.add(value2.fio)
                            childMap[value1.group] = tempList
                        }
                    }

                }
                val adapter = CustomAdapter(this,courseList,childMap)
                binding.expandableListViewGroupFio.setAdapter(adapter)
            } else {
                binding.emptyStudents.isVisible = true
            }
        }
        binding.expandableListViewGroupFio.setOnChildClickListener{ _, _, groupP, childP, _ ->
            val date = courseList[groupP]
            val time = childMap[date]!![childP]

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