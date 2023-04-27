package com.denisal.studentsmarks.viewsgrade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityGradeViewBinding
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import com.denisal.studentsmarks.viewsgrade.adapters.ViewAdapterGrade
import com.denisal.studentsmarks.viewsgrade.viewmodels.GradeDataView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class GradeViewActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityGradeViewBinding
    private val array: MutableList<GradeDataView> = arrayListOf()
    var paramSearch = "course.name ASC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeViewBinding.inflate(layoutInflater)
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
                .setMessage("В этом окне можно просмотреть успеваемость студентов, и" +
                    " задать фильтр показа успеваемости, " +
                            "нажатием на стрелочку вниз и выбором типа фильтрации, также при нажатии на нужное поле можно изменить данные")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        binding.show.setOnClickListener{
            if (binding.filterLayout.isVisible) {
                binding.show.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                binding.filterLayout.isVisible = false
            } else {
                binding.show.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                binding.filterLayout.isVisible = true
            }

        }
        val listFilter: List<String> = listOf("По имени предмета(по возр.)", "По имени предмета(по убыв.)",
            "По имени занятия(по возр.)", "По имени занятия(по убыв.)",
            "По дате сдачи(по возр.)", "По дате сдачи(по убыв.)" )
        val spinnerFilter: Spinner = findViewById(R.id.spinnerFilter)
        spinnerFilter.onItemSelectedListener = this
        val add: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, listFilter)
        add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = add
    }
    private fun finishLoad(array: MutableList<GradeDataView>) {
        val recyclerViewMovieList: RecyclerView = findViewById(R.id.recyclerViewTraffic)
        if (recyclerViewMovieList != null) {
            binding.processLoading.isVisible = false
            binding.horizontalScrollView.isVisible = true
            recyclerViewMovieList.removeAllViewsInLayout();
            recyclerViewMovieList.layoutManager = LinearLayoutManager(applicationContext)
            recyclerViewMovieList.adapter = ViewAdapterGrade(array, this@GradeViewActivity)
        }
    }


    override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
        val spinnerFilterCheck: Spinner = findViewById(R.id.spinnerFilter)
        binding.processLoading.isVisible = true
        binding.horizontalScrollView.isVisible = false

        if(p0.id == R.id.spinnerFilter) {
            if (spinnerFilterCheck.selectedItem == "По имени предмета(по возр.)") {
                paramSearch = "course.name ASC"
            }
            if (spinnerFilterCheck.selectedItem == "По имени предмета(по убыв.)") {
                paramSearch = "course.name DESC"
            }
            if (spinnerFilterCheck.selectedItem == "По имени занятия(по возр.)") {
                paramSearch = "lesson.name ASC"
            }
            if (spinnerFilterCheck.selectedItem == "По имени занятия(по убыв.)") {
                paramSearch = "lesson.name DESC"
            }
            if (spinnerFilterCheck.selectedItem == "По дате сдачи(по возр.)") {
                paramSearch = "lesson.date ASC"
            }
            if (spinnerFilterCheck.selectedItem == "По дате сдачи(по убыв.)") {
                paramSearch = "lesson.date DESC"
            }
            loadArray()
            binding.show.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            binding.filterLayout.isVisible = false

        }
    }

    private fun loadArray(){
        Thread{
            array.clear()
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
               val sql = "SELECT assessment.id, course.name, lesson.name, task.name, student.studGroup, student.fullName, " +
               "assessment.date, assessment.value FROM assessment INNER JOIN student ON student_id = student.id " +
               "INNER JOIN task ON task_id = task.id INNER JOIN lesson ON lesson_id = lesson.id " +
               "INNER JOIN course ON task.course_id = course.id WHERE student.teacher_id = $teacherID ORDER BY $paramSearch"
//                val sql = "SELECT traffic.id, traffic.student_id, lesson.date, course.name, lesson.name, student.fullName, " +
//                        "traffic.lesson_id FROM traffic INNER JOIN student ON student_id = student.id INNER JOIN lesson ON traffic.lesson_id = lesson.id " +
//                        "INNER JOIN course ON lesson.course_id = course.id WHERE student.teacher_id = $teacherID ORDER BY $paramSearch"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val assessmentID = result.getInt(1)
                    val courseName = result.getString(2)
                    val lessonName = result.getString(3)
                    val taskName = result.getString(4)
                    val group = result.getString(5)
                    val fio = result.getString(6)
                    val date = result.getString(7)
                    val value = result.getString(8)
                    array.add(GradeDataView(assessmentID, courseName, lessonName, taskName, group, fio, date, value))
                }
                cn.close()

            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            runOnUiThread {
                finishLoad(array)
            }
        }.start()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    fun onClick(position: Int) {
        val id = array[position - 1].assessmentID
        val value = array[position - 1].value
        val fio = array[position - 1].fio
        val group = array[position - 1].group
        val task = array[position - 1].taskName
        val lesson = array[position - 1].lessonName
        val course = array[position - 1].courseName
        val newActivity = "$id|$value|$fio|$group|$task|$lesson|$course"
        val intent = Intent(this, EditAssessmentActivity::class.java)
        intent.putExtra("arrayEdit", newActivity)
        startActivity(intent)
        finish()
    }
}