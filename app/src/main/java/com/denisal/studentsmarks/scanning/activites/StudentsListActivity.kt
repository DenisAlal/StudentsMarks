package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.emptyStudents.isVisible = false
        if(studentArrayCheck.isEmpty()) {
            binding.emptyStudents.isVisible = true
            binding.recyclerViewStudents.isVisible = false
        } else {
            val adapter = StudentsAdapterQR(studentArrayCheck)
            binding.recyclerViewStudents.adapter = adapter
        }
    }
}