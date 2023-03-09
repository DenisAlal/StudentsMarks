package com.denisal.studentsmarks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.denisal.studentsmarks.auth.SignInActivity

class SplashActivity : AppCompatActivity() {
    private var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mHandler = Handler()
        mHandler!!.postDelayed(runnable, 1500)
    }
    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}