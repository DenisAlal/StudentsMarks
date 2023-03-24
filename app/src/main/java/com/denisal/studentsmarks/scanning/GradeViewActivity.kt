package com.denisal.studentsmarks.scanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.denisal.studentsmarks.DataListCourseAndLesson
import com.denisal.studentsmarks.LessonData
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.databinding.ActivityGradeViewBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB

class GradeViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGradeViewBinding
    val db = GetFromDB()
    private var courseArray: MutableList<SubjData> = mutableListOf()
    private var lessonArray: MutableList<LessonData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Просмотр успеваемости"



        courseArray = db.getDataCourse()
        lessonArray = db.getDataLesson()
        val tempListsID = mutableListOf<DataListCourseAndLesson>()
        val courseList = mutableListOf<String>()
        val childMap = mutableMapOf<String,List<String>>()
        var i1 = 0

        if (courseArray.isNotEmpty() && lessonArray.isNotEmpty()) {
            for (index in courseArray.indices) {
                var i2 = 0
                val courseArrayValue = courseArray[index]
                val tempList = mutableListOf<String>()
                courseList.add(courseArrayValue.name)
                for (index2 in lessonArray.indices) {
                    val value = lessonArray[index2]
                    if(value.courseId == courseArrayValue.id) {
                        tempList.add(value.name)
                        tempListsID.add(DataListCourseAndLesson(i1, i2,
                            courseArrayValue.id, value.id))
                        i2++
                        childMap[courseArrayValue.name] = tempList
                    }
                }
                i1++
            }
            val adapter = CustomAdapter(this,courseList,childMap)
            binding.expandableListView.setAdapter(adapter)
        } else {
            Toast.makeText(applicationContext, "Нет данных", Toast.LENGTH_LONG).show()
        }
        binding.expandableListView.setOnChildClickListener{ _, _, groupP, childP, _ ->
            val course = courseList[groupP]
            val lesson = childMap[course]!![childP]
            Log.e("adfawdawdawdw", "array = $tempListsID")
            Log.e("Check position", "курс $groupP ----- группа $childP")

            for (index in tempListsID.indices) {
                val value = tempListsID[index]
                if(value.posCourse == groupP && value.posLesson == childP) {
                    goNext(course, lesson, value.idCourse, value.idLesson)
                }
            }
            true
        }
    }
    private fun goNext(course: String, lesson: String, idCourse: Int, idLesson: Int) {
        val intent = Intent(this, SetTaskAndFIOActivity::class.java)
        intent.putExtra("course", course)
        intent.putExtra("lesson", lesson)
        intent.putExtra("idCourse", idCourse)
        intent.putExtra("idLesson", idLesson)
        Log.e("Check position", "idкурс $idCourse ----- idгруппа $idLesson")
        startActivity(intent)
        Toast.makeText(this,"$course  $lesson $idCourse $idLesson", Toast.LENGTH_SHORT).show()
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