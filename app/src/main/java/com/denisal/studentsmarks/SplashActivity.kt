package com.denisal.studentsmarks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.denisal.studentsmarks.auth.SignInActivity

class SplashActivity : AppCompatActivity() {
    private var mHandler: Handler? = null
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val nightMode = sharedPref.getBoolean("nightMode", false)
        if (nightMode) {
            Log.e("132", "dark")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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