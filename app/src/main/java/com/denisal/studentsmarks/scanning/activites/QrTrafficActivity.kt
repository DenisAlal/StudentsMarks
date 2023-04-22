package com.denisal.studentsmarks.scanning.activites

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.db.GetFromDB
import com.denisal.studentsmarks.db.InsertToDB
import com.google.android.material.floatingactionbutton.FloatingActionButton


class QrTrafficActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    val db = GetFromDB()
    private val insert = InsertToDB()
    private val arrayStudents = db.checkStudentQR()
    private val checkedStudents: MutableList<StudentsData> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_traffic)
        val back: FloatingActionButton = findViewById(R.id.goBack)
        back.setOnClickListener{
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val info: FloatingActionButton = findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(this)
                .setTitle("Информация")
                .setMessage("В данном меню можно отсканировать QR коды студентов")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }

        if (ContextCompat.checkSelfPermission(this@QrTrafficActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@QrTrafficActivity, arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            startScanning()
        }
        val butList: Button = findViewById(R.id.checkList)
        butList.setOnClickListener{
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            val intent = Intent(this, StudentsListActivity::class.java)
            startActivity(intent)
        }
        val butSave: Button = findViewById(R.id.saveScanning)
        butSave.setOnClickListener{

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
        val change = findViewById<ImageButton>(R.id.changeCam)
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
        // Parameters (default values)
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or SINGLE
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                addArray(it.text.split("|"), codeScanner) }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(applicationContext, "Ошибка камеры: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun addArray(listStudentQR: List<String>, codeScanner: CodeScanner) {
        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.notification)
        if(arrayStudents.any{ it.fullName == listStudentQR[0]} && arrayStudents.any{ it.group == listStudentQR[1]}) {
            if (studentArrayCheck.any{ it.fio == listStudentQR[0]} && studentArrayCheck.any{ it.group == listStudentQR[1]}) {
                val mDialogFail = LayoutInflater.from(this).inflate(R.layout.fail, null)
                val mBuilderFail = AlertDialog.Builder(this)
                    .setView(mDialogFail).setTitle("Данный студент уже отсканирован")
                val windowFail = mBuilderFail.show()
                object : CountDownTimer(2000, 600){
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        this@QrTrafficActivity.codeScanner.startPreview()
                        windowFail.dismiss()
                    }
                }.start()
            } else  {
                val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
                mp.start()
                val mBuilderSuccess = AlertDialog.Builder(this)
                    .setView(mDialogSuccess).setTitle("Студент добавлен в список")
                val windowSuccess = mBuilderSuccess.show()
                object : CountDownTimer(2000, 600){
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("tik", "tak")
                    }
                    override fun onFinish() {
                        this@QrTrafficActivity.codeScanner.startPreview()
                        windowSuccess.dismiss()
                        mp.stop()

                    }
                }.start()
                studentArrayCheck.add(StudentsArray(listStudentQR[0], listStudentQR[1]))
                val student: StudentsData? = arrayStudents.find { it.fullName == listStudentQR[0] && it.group == listStudentQR[0] }
                if (student != null) {
                    checkedStudents.add(student)
                    Log.e("", checkedStudents.toString())
                }

            }
        } else {
            val mDialogFail = LayoutInflater.from(this).inflate(R.layout.fail, null)
            val mBuilderFail = AlertDialog.Builder(this)
                .setView(mDialogFail).setTitle("Данного студента нет в списке стуентов преподавателя")
            val windowFail = mBuilderFail.show()
            object : CountDownTimer(2000, 600){
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("tik", "tak")
                }
                override fun onFinish() {
                    this@QrTrafficActivity.codeScanner.startPreview()
                    windowFail.dismiss()
                }
            }.start()
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