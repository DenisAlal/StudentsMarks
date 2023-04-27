package com.denisal.studentsmarks.viewsgrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityTrafficViewBinding
import com.denisal.studentsmarks.db.assessments.AssesDataRoom
import com.denisal.studentsmarks.db.assessments.AssessDB
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.scanning.adapters.AssessSessionAdapter
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import com.denisal.studentsmarks.viewsgrade.adapters.ViewAdapterTraffic
import com.denisal.studentsmarks.viewsgrade.viewmodels.TrafficDataView
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class TrafficViewActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityTrafficViewBinding
    private val array: MutableList<TrafficDataView> = arrayListOf()
    var paramSearch = "course.name ASC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrafficViewBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.goBack.setOnClickListener{
            finish()
        }
        binding.goInfo.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В этом окне можно просмотреть посещаемость студентов, и" +
                        " задать фильтр показа посещаемость, " +
                        "нажатием на стрелочку вниз и выбором типа фильтрации," +
                        " также при нажатии на нужное поле можно изменить данные")
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
//        binding.filterButton.setOnClickListener{
//            binding.show.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
//            binding.filterLayout.isVisible = false
//        }


        val listFilter: List<String> = listOf("По имени предмета(по возр.)", "По имени предмета(по убыв.)",
            "По имени занятия(по возр.)", "По имени занятия(по убыв.)",
            "По дате занятия(по возр.)", "По дате занятия(по убыв.)" )
        val spinnerFilter: Spinner = findViewById(R.id.spinnerFilter)
        spinnerFilter.onItemSelectedListener = this
        val add: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, listFilter)
        add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = add
    }
    private fun finishLoad(array: MutableList<TrafficDataView>) {
        val recyclerViewMovieList: RecyclerView = findViewById(R.id.recyclerViewTraffic)
        if (recyclerViewMovieList != null) {
            binding.processLoading.isVisible = false
            binding.horizontalScrollView.isVisible = true
            recyclerViewMovieList.removeAllViewsInLayout();
            recyclerViewMovieList.layoutManager = LinearLayoutManager(applicationContext)
            recyclerViewMovieList.adapter = ViewAdapterTraffic(array, this@TrafficViewActivity)
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
            if (spinnerFilterCheck.selectedItem == "По дате занятия(по возр.)") {
                paramSearch = "lesson.date ASC"
            }
            if (spinnerFilterCheck.selectedItem == "По дате занятия(по убыв.)") {
                paramSearch = "lesson.date DESC"
            }
            loadArray()
            binding.show.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            binding.filterLayout.isVisible = false

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
    private fun loadArray(){
        Thread{
            array.clear()
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
//               val sql = "SELECT  * FROM assessment INNER JOIN student ON student_id = student.id INNER JOIN task  " +
//                        "ON task_id = task.id INNER JOIN lesson ON lesson_id = lesson.id INNER JOIN " +
//                        "course ON task.course_id = course.id  WHERE student.teacher_id = '$teacherID' ORDER BY course.name ASC"
                val sql = "SELECT traffic.id, traffic.student_id, lesson.date, course.name, lesson.name, student.fullName, " +
                        "traffic.lesson_id FROM traffic INNER JOIN student ON student_id = student.id INNER JOIN lesson ON traffic.lesson_id = lesson.id " +
                        "INNER JOIN course ON lesson.course_id = course.id WHERE student.teacher_id = $teacherID ORDER BY $paramSearch"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val trafficID = result.getInt(1)
                    val studentID = result.getInt(2)
                    val date = result.getString(3)
                    val courseName = result.getString(4)
                    val lessonName = result.getString(5)
                    val fio = result.getString(6)
                    val lessonID = result.getInt(7)
                    array.add(TrafficDataView(trafficID, studentID,date, courseName, lessonName, fio, lessonID))
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

    fun onClick(position: Int) {
        val builderSucceed = AlertDialog.Builder(this)
            .setTitle("Удалить посещаемость?")
            .setMessage("Подтвердите удаление посещаемости студента за выбранную дату")
            .setIcon(R.drawable.baseline_error_outline_24_orange)
        builderSucceed.setNegativeButton("Удалить"){ _, _ ->

            Thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert = "DELETE FROM traffic WHERE id = ${array[position - 1].id}"
                    ps.execute(insert)
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                runOnUiThread{loadArray()}
            }.start()

        }
        builderSucceed.setPositiveButton("Отмена"){ _, _ ->

        }

        val alertDialogSuccess: AlertDialog = builderSucceed.create()
        alertDialogSuccess.show()

    }

}