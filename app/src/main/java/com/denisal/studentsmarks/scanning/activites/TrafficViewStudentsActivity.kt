package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityTrafficViewStudentsBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.scanning.CustomAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TrafficViewStudentsActivity : AppCompatActivity() {
    val db = GetFromDB()
    private lateinit var binding: ActivityTrafficViewStudentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrafficViewStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idLessonTraffic: Int = intent.getIntExtra("IDLesson", -1)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно посмотреть на посещаемость студентов")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
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
                val builderSucceed = AlertDialog.Builder(this)
                    .setTitle("Ошибка загрузки")
                    .setMessage(
                        "Внимание! Список занятий пуст," +
                                " для отображения списка занятий необходимо их добавить!"
                    )
                    .setIcon(R.drawable.baseline_error_outline_24_orange)
                builderSucceed.setNegativeButton("ОК") { _, _ ->
                    finish()
                }
                val alertDialogSuccess: AlertDialog = builderSucceed.create()
                alertDialogSuccess.show()
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