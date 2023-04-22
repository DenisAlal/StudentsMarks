package com.denisal.studentsmarks.scanning.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.SubjData
import com.denisal.studentsmarks.databinding.ActivityCreateTaskBinding
import com.denisal.studentsmarks.db.GetFromDB
import com.denisal.studentsmarks.db.InsertToDB
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateTaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var subjArray: MutableList<SubjData> = mutableListOf()
    private val db = GetFromDB()
    private val insertToDB = InsertToDB()
    private lateinit var binding: ActivityCreateTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
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
                .setMessage("В данном меню можно выбрать создать задание")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        subjArray = db.getDataCourse()
        init()
    }

    private fun init() {
        val saveTask: Button = findViewById(R.id.saveTask)
        val spinCourse: Spinner = findViewById(R.id.choiceCourse)

        val nameTask: TextView = findViewById(R.id.nameTask)
        saveTask.setOnClickListener {
            val position = spinCourse.selectedItemPosition
            if (nameTask.text.isNotEmpty()) {


                val check = insertToDB.insertTask(nameTask.text.toString(), subjArray[position].id)
                if (check) {
                    val mBuilderSuccess = android.app.AlertDialog.Builder(this)
                        .setTitle("Задание добавлено")
                        .setMessage("Добавить еще одно задание?")
                        .setIcon(R.drawable.baseline_check_circle_24)
                    mBuilderSuccess.setPositiveButton("Да") { _, _ ->
                        nameTask.text = ""
                    }
                    mBuilderSuccess.setNegativeButton("Нет") { _, _ ->
                        nameTask.text = ""
                        finish()
                    }
                    val alertDialogError: android.app.AlertDialog = mBuilderSuccess.create()
                    alertDialogError.show()
                }
            } else {
                Toast.makeText(applicationContext, "Введите название задания", Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (subjArray.isNotEmpty()) {
            val arrayForSpinnerSubj: ArrayList<String> = arrayListOf()
            for (index in subjArray.indices) {
                val value = subjArray[index]
                arrayForSpinnerSubj.add(value.name)
            }

            spinCourse.onItemSelectedListener = this
            val add: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                arrayForSpinnerSubj as List<Any?>
            )
            add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinCourse.adapter = add

        } else {
            spinCourse.isVisible = false
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if (parent.id == R.id.choiceCourse) {
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}