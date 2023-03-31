package com.denisal.studentsmarks.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.denisal.studentsmarks.R
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var resetPass: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Восстановление пароля"
        val reset: Button = findViewById(R.id.resetPassword)
        auth = FirebaseAuth.getInstance()
        resetPass = findViewById(R.id.emailText)
        reset.setOnClickListener {
            resetPassword(resetPass.text.toString())
        }
    }

    private fun resetPassword(toString: String) {
        auth.sendPasswordResetEmail(toString)
            .addOnSuccessListener {
                val mBuilderSuccess = AlertDialog.Builder(this)
                    .setMessage("Сообщение с востановлением пароля отправлено на указанную вами почту")
                val windowSuccess = mBuilderSuccess.show()
                object : CountDownTimer(1800, 600) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        windowSuccess.dismiss()
                        finish()
                    }
                }.start()
            }
            .addOnFailureListener {
                val mBuilderFail = AlertDialog.Builder(this)
                    .setMessage(it.toString())
                    .setTitle("Ошибка")
                val windowFail = mBuilderFail.show()
                object : CountDownTimer(2400, 600) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        windowFail.dismiss()
                    }
                }.start()
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