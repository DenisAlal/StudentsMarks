package com.denisal.studentsmarks.settingsactivity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.databinding.ActivityDeleteInfoBinding
import com.denisal.studentsmarks.dbfunctions.GetFromDB
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class DeleteInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Удаление данных"
        val btnAccept: Button = binding.OK
        val removeStudents: CheckBox = binding.studentsCheck
        val removeDB: CheckBox = binding.gradeDelete
        val accept: EditText = binding.acceptText
        val getID = GetFromDB()
        getID.get()
        removeStudents.setOnClickListener{
            binding.gradeDelete.isChecked = removeStudents.isChecked
        }
        btnAccept.setOnClickListener{
            val checkAcc = accept.text.toString()
            if(checkAcc == "I agree"){
                if (removeStudents.isChecked) {
                    Thread(Runnable {
                        deleteStudList()
                    }).start()
                }
                if (removeDB.isChecked) {
                    Thread(Runnable {
                        deleteDB()
                    }).start()
                }
            } else {
                val errorToast = Toast.makeText(
                    this@DeleteInfoActivity,
                    "Ошибка, введите подтверждение!",
                    Toast.LENGTH_SHORT
                )
                errorToast.show()
            }

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
    private fun deleteStudList() {
        Log.e("delete stud", "delete stud")
        runOnUiThread {}
        if (teacherID != -1) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val ps = cn.createStatement()
                val insert = "DELETE FROM student WHERE teacher_id='$teacherID'"
                ps.execute(insert)
                if (ps != null) {
                    ps!!.close()
                }
                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            runOnUiThread {  }
        }
    }
    private fun deleteDB() {

        Log.e("delete DB", "delete DB")
    }
}