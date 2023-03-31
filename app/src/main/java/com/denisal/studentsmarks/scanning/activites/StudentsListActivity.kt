package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivityStudentsListBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import com.denisal.studentsmarks.scanning.StudentsAdapterQR
import com.denisal.studentsmarks.studentArrayCheck

class StudentsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentsListBinding
    val db = GetFromDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        binding.recyclerViewStudents.layoutManager = LinearLayoutManager(this)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Список студентов"
        if(studentArrayCheck.isEmpty()) {
            binding.recyclerViewStudents.isVisible = false
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Ошибка загрузки")
                .setMessage(
                    "Нет сведений о студентах"
                )
                .setIcon(R.drawable.baseline_error_outline_24_orange)
            builderSucceed.setNegativeButton("ОК") { _, _ ->
                finish()
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        } else {
            val adapter = StudentsAdapterQR(studentArrayCheck)
            binding.recyclerViewStudents.adapter = adapter
        }
    }
}