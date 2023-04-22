package com.denisal.studentsmarks.scanning.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denisal.studentsmarks.HomeActivity
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.databinding.ActivitySessionBinding
import com.denisal.studentsmarks.db.session.SessionDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val dbSession = SessionDB.getDB(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goBack.setOnClickListener{
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.closeSession.setOnClickListener{
            thread{
                dbSession.getSessionDao().deleteSession()
                dbSession.getSessionDao().resetAutoIncrementValueSession()
            }.join()
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.scanQR.setOnClickListener{
            finish()
            val intent = Intent(this, QrTrafficActivity::class.java)
            startActivity(intent)
        }
    }

}