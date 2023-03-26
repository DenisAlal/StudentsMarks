package com.denisal.studentsmarks.scanning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivitySetTaskAndFioactivityBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB

class SetTaskAndFIOActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySetTaskAndFioactivityBinding
    private var data: MutableList<FullData> = mutableListOf()
    private var selectedData: MutableList<TaskListFIOAndLesson> = mutableListOf()
    val db = GetFromDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetTaskAndFioactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"
        binding.emptyViewTasks.isVisible = false
        val strCourse: String? = intent.getStringExtra("course")
        val strLesson: String? = intent.getStringExtra("lesson")

        val strIDCourse: Int = intent.getIntExtra("idCourse",-1)
        val strIDLesson: Int = intent.getIntExtra("idLesson", -1)
        if (strIDCourse != -1 && strIDLesson != -1) {
            data = db.getFullData(strIDCourse, strIDLesson)
            Log.e("", data.toString())
            selectedData.clear()
            if (data.isNotEmpty()) {
                val studentList = mutableListOf<String>()
                val childMap = mutableMapOf<String,List<String>>()
                var i = 0
                for (index in data.indices) {
                    var i2 = 0
                    val valueFio = data[index]
                    var check = false
                    val tempList = mutableListOf<String>()
                    if (!studentList.contains(valueFio.fullName)) {
                        studentList.add(valueFio.fullName)
                        check = true
                    }
                    for (index2 in studentList.indices) {
                        val vallue = studentList[index2]
                        if (vallue == valueFio.fullName) {
                            for (index3 in data.indices) {
                                val valueTask = data[index3]
                                if(valueTask.fullName == valueFio.fullName) {
                                    tempList.add("${valueTask.name}: ${valueTask.value}")
                                    selectedData.add(TaskListFIOAndLesson(i, i2,
                                        valueFio.idStud, valueTask.id, valueTask.name, valueTask.studGroup))
                                    i2++
                                    childMap[valueFio.fullName] = tempList
                                }
                            }
                        }
                    }

                    if (check) {
                        i++
                    }

                }
                val adapter = CustomAdapter(this,studentList,childMap)
                binding.expandableListViewTasks.setAdapter(adapter)
                binding.expandableListViewTasks.setOnChildClickListener{ _, _, groupP, childP, _ ->
                    val FIO = studentList[groupP]
                    val lesson = childMap[FIO]!![childP]
                    for(index in selectedData.indices) {
                        val value = selectedData[index]
                        if(value.posFIO == groupP && value.posMark == childP) {
                            goNext(value.idMark, value.taskName, value.groupStud, FIO)
                        }
                    }
                    true
                }
            } else {
                binding.emptyTasks.isVisible = false
                binding.emptyViewTasks.isVisible = true
            }
        } else {
            Log.e("bundle", "empty")
        }
    }
    private fun goNext(assessment: Int, taskName: String, group: String, FIO: String) {
        val intent = Intent(this, ChangeAssessmentActivity::class.java)
        intent.putExtra("Assessment", assessment)
        intent.putExtra("Group", group)
        intent.putExtra("FIO", FIO)
        intent.putExtra("TaskName", taskName)
        startActivity(intent)
        Toast.makeText(this,"$assessment", Toast.LENGTH_SHORT).show()
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

