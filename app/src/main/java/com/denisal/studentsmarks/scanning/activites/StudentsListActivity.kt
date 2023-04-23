package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.StudentsArray
import com.denisal.studentsmarks.databinding.ActivityStudentsListBinding
import com.denisal.studentsmarks.db.students.StudentsDB
import com.denisal.studentsmarks.scanning.StudentsAdapterQR
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class StudentsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню отображены отсканированные студенты")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        val dbStudents = StudentsDB.getDB(this)
        val array: MutableList<StudentsArray> = arrayListOf()
        dbStudents.getStudentsDao().getStudents().asLiveData().observe(this) {List ->
            array.clear()
            List.forEach{
                array.add(StudentsArray(it.fio, it.group))
            }
            val adapter = StudentsAdapterQR(array)
            binding.recyclerViewStudents.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewStudents.adapter = adapter
        }
    }
}