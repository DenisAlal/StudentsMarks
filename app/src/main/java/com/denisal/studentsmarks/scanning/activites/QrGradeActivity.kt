package com.denisal.studentsmarks.scanning.activites

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.denisal.studentsmarks.*

class QrGradeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Сканирование успеваемости студента"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_grade)
        val actionBar = supportActionBar
        Log.e("task_id", "$id_task")
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(this@QrGradeActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@QrGradeActivity, arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            startScanning()
        }
        val choose: Button = findViewById(R.id.choose)
        choose.setOnClickListener{
            if (id_task !=  -1  && listStudent.isNotEmpty()){
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                val intent = Intent(this, AssessmentActivity::class.java)
                startActivity(intent)
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

    private fun startScanning() {
        val change: Button = findViewById(R.id.changeCamGrade)
        var i = 0
        change.setOnClickListener {
            if (i == 0) {
                codeScanner.camera = CodeScanner.CAMERA_FRONT
                i = 1
            } else {
                codeScanner.camera = CodeScanner.CAMERA_BACK
                i = 0
            }
        }
        val scannerView: CodeScannerView = findViewById(R.id.scanner_viewGrade)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or SINGLE
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val listStudentsSplit: List<String> = it.text.split("|")
                listStudent.add(listStudentsSplit[0])
                listStudent.add(listStudentsSplit[1])
                Toast.makeText(this, "ФИО: ${listStudentsSplit[0]}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
               Toast.makeText(applicationContext, "Ошибка камеры: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
        val restart: Button = findViewById(R.id.restart)
        restart.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Доступ к камере получен", Toast.LENGTH_LONG).show()
                startScanning()
            } else {
                Toast.makeText(this, "Отказано в доступе к камере", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if(::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }
}