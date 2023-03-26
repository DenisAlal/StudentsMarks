package com.denisal.studentsmarks.scanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.denisal.studentsmarks.databinding.ActivityChangeAssessmentBinding

class ChangeAssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeAssessmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Изменение оценки"
        val strIDmark: Int = intent.getIntExtra("Assessment", -1)
        val strGroup: String? = intent.getStringExtra("Group")
        val strFIO: String? = intent.getStringExtra("FIO")
        val strNameTask: String? = intent.getStringExtra("TaskName")
        Log.e("123", "$strIDmark")
        binding.group.text = "${binding.group.text} $strGroup"
        binding.student.text = "${binding.student.text}  $strFIO"
        binding.task.text = "${binding.task.text}  $strNameTask"
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