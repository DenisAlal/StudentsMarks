package com.denisal.studentsmarks.settingsactivity
import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.denisal.studentsmarks.*
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileOutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import com.denisal.studentsmarks.databinding.ActivityLoadAndSendStudentsBinding
import com.denisal.studentsmarks.db.InsertToDB
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LoadAndSendStudentsActivity : AppCompatActivity() {
        private lateinit var binding: ActivityLoadAndSendStudentsBinding
        private val tag: String = "main"
        private val requestCode = 101
        private val pickFile = 100
        private var file: File? = null
        private var fileUri: Uri? = null
        private var fileName: String? = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoadAndSendStudentsBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            binding.sendDataStud.isEnabled = false
            binding.process.isVisible = false
            binding.alert.isVisible = false
            Log.e("","$teacherID")
            init()
        }

        private fun deleteFile () {
            val dir = File(this.filesDir, "doc")
            file = File(dir, filenameDelete)
            if (isFileExists(file!!)) {
                Log.e("File", "abobaaa!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            } else {
                Log.e("File", "deleted!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
            val d0: Boolean = file?.delete() == true
            Log.w("Delete Check", "File deleted: $dir/myFile $d0")
            if (isFileExists(file!!)) {
                Log.e("File", "abobaaa!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            } else {
                Log.e("File", "deleted!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
        }
        private fun isFileExists(file: File): Boolean {
            return file.exists() && !file.isDirectory
        }
        private fun init() {
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
            )
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
            )
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
            )
            val info: FloatingActionButton = findViewById(R.id.goInfo)
            info.setOnClickListener{
                val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.example_load_dialog, null)
                val builderSucceed = AlertDialog.Builder(this)
                    .setTitle("Информация")
                    .setMessage("На этой вкладке можно добавить информацию о студентах, " +
                            "чтобы добавить студентов с помощью файла необходимо " +
                            " ФИО должно быть без пробелов в начале " +
                            "и в конце, между фио по одноиу пробелу, файл сохранить нужно в формате .xlsx, " +
                            "ниже пример того как должна быть устроена таблица")
                    .setIcon(R.drawable.outline_info_24)
                    .setView(mDialogSuccess)
                builderSucceed.setPositiveButton("OK"){ _, _ ->
                }
                val alertDialogSuccess: AlertDialog = builderSucceed.create()
                alertDialogSuccess.show()
            }
            setupClickListener()
        }
        private fun setupClickListener() {
            val butLoad = binding.loadStudents
            butLoad.setOnClickListener{
                checkForStoragePermission();
            }
            val butSend = binding.sendDataStud
            butSend.setOnClickListener{
                Thread(Runnable {
                    sendData()
                }).start()

            }
            binding.loadStudentsManual.setOnClickListener{
                val insert = InsertToDB()
                val fioCheck = binding.fioET.text.toString()
                val group = binding.groupET.text.toString()
                if (fioCheck.isNotEmpty() && group.isNotEmpty()) {
                    val check = insert.insertOneStudent(fioCheck,group)
                    if(check) {
                        val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
                        val mBuilderSuccess = AlertDialog.Builder(this)
                            .setView(mDialogSuccess).setTitle("Данные загружены")
                        val windowSuccess = mBuilderSuccess.show()
                        object : CountDownTimer(1800, 600){
                            override fun onTick(millisUntilFinished: Long) {
                                Log.e("tik", "tak")
                            }
                            override fun onFinish() {
                                windowSuccess.dismiss()
                            }
                        }.start()
                    } else {
                        Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Необходимо заполнить все поля!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.goBack.setOnClickListener{
                finish()
            }
        }
        private fun checkForStoragePermission() {
            if (ContextCompat.checkSelfPermission(
                    this@LoadAndSendStudentsActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this@LoadAndSendStudentsActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    requestCode
                )
                openDocument()
            } else {
                openDocument()
            }
        }
        private fun openDocument() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                addCategory(Intent.CATEGORY_OPENABLE)
            }
                startActivityForResult(intent, pickFile)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            var newUri = ""
            when(requestCode) {
                pickFile -> {
                    if (resultCode == Activity.RESULT_OK) {
                        var mimeTypeExtension: String? = ""
                        data?.data?.also { uri ->
                            Log.e(tag, "ApachPOI Selected file Uri : " + uri)
                            mimeTypeExtension = uri.getExtention(this)
                            Log.e(tag, "ApachPOI Selected file mimeTypeExtension : " + mimeTypeExtension)
                            if (mimeTypeExtension != null && mimeTypeExtension?.isNotEmpty() == true) {
                                if (mimeTypeExtension?.contentEquals("xlsx") == true
                                    || mimeTypeExtension?.contentEquals("xls") == true
                                ) {
                                    Log.e(
                                        tag,
                                        "ApachPOI Selected file mimeTypeExtension valid : " + mimeTypeExtension
                                    )
                                } else {
                                    Toast.makeText(this, "invalid file selected", Toast.LENGTH_SHORT).show()
                                    return
                                }
                            }
                            newUri = uri.toString()
                            copyFileAndExtract(newUri)
                            Log.e("Uri-------------------" , uri.toString())
                        }
                    }
                }
            }

        }

        private fun Uri.getExtention(context: Context): String? {
            var extension: String? = ""
            extension = if (this.scheme == ContentResolver.SCHEME_CONTENT) {
                val mime = MimeTypeMap.getSingleton()
                mime.getExtensionFromMimeType(context.contentResolver.getType(this))
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    MimeTypeMap.getFileExtensionFromUrl(
                        FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            File(this.path)
                        ).toString()
                    )
                } else {
                    MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(this.path)).toString())
                }
            }
            return extension
        }
        private fun copyFileAndExtract(uri: String) {
            val dir = File(this.filesDir, "doc")
            dir.mkdirs()
            fileName = getFileName(uri.toUri())
            filenameDelete = fileName.toString()
            file = File(dir, fileName)
            file?.createNewFile()
            val fileOut = FileOutputStream(file)
            try {
                contentResolver.openInputStream(uri.toUri())?.use { inputStream ->
                    fileOut.use { output ->
                        inputStream.copyTo(output)
                        output.flush()
                    }
                }
                fileUri = FileProvider.getUriForFile(this, "$packageName.provider", file!!)
            } catch (e: Exception) {
                fileUri = uri.toUri()
                e.printStackTrace()
            }
            fileUri?.apply {
                file?.apply {
                    Log.e("absolute path", this.absolutePath)
                    exelReader(this.absolutePath)
                }
            }
        }
        private fun getFileName(uri: Uri): String? = when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
            else -> uri.path?.let(::File)?.name
        }
        private fun getContentFileName(uri: Uri): String? = runCatching {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
                return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    .let(cursor::getString)
            }
        }.getOrNull()


        private fun exelReader(path: String) {
            val workbook: Workbook = WorkbookFactory.create(File(path))
            val sheet: Sheet = workbook.getSheetAt(0)
            val dataFormatter = DataFormatter()
            studData.clear()
            sheet.forEach { row ->
                var newGroup = ""
                var val1 = ""
                var val2 = ""
                var val3 = ""
                var val4 = ""
                var ignore = false
                var i = "1"
                row.forEach { cell ->
                    if (cell.toString().toLowerCase() == "номер" ||
                        cell.toString().toLowerCase() == "группа" ||
                        cell.toString().toLowerCase() == "подгруппа" ||
                        cell.toString().toLowerCase() == "фио") {
                        Log.w("parsing", "find header")
                        ignore = true
                    } else {
                        ignore = false
                        when(i) {
                            "4" -> {
                                if (cell.cellType === Cell.CELL_TYPE_FORMULA) {
                                    val4 = cellCheck(cell)
                                } else {
                                    val4 = dataFormatter.formatCellValue(cell)
                                }
                            }
                            "3" -> {
                                i = "4"
                                if (cell.cellType === Cell.CELL_TYPE_FORMULA) {
                                    val3 = cellCheck(cell)
                                } else {
                                    val3 = dataFormatter.formatCellValue(cell)
                                }
                            }
                            "2" -> {
                                i = "3"
                                if (cell.cellType === Cell.CELL_TYPE_FORMULA) {
                                    val2 = cellCheck(cell)
                                } else {
                                    val2 = dataFormatter.formatCellValue(cell)
                                }
                            }
                            "1" -> {
                                i = "2"
                                if (cell.cellType === Cell.CELL_TYPE_FORMULA) {
                                    val1 = cellCheck(cell)
                                } else {
                                    val1 = dataFormatter.formatCellValue(cell)
                                }
                            }
                        }
                    }
                }
                if (!ignore) {
                    when(val3) {
                        "Подг А" -> {
                            newGroup = "$val2.1"
                        }
                        "Подг Б" -> {
                            newGroup = "$val2.2"
                        }
                        "Подг В" -> {
                            newGroup = "$val2.3"
                        }
                        "Подг Г" -> {
                            newGroup = "$val2.4"
                        }
                    }
                    val convertToInt: Int = val1.toInt()
                    studData.add(StudentsData(convertToInt, newGroup, val4))
                    Log.e("studData", studData.toString())
                    Log.w("read exel", "$val1 $val2 $val3 $val4")
                } else {
                    Log.w("read exel", "ignore")
                }
            }
            Log.e("teacher_id", teacherID.toString())
            binding.alert.isVisible = true
            binding.alert.text = "Данные из файла загружены, для их " +
                    "сохранения нажмите кнопку отправить данные"
            binding.sendDataStud.isEnabled = true;
        }

        private fun sendData() {
            runOnUiThread { startProcess() }
            if (teacherID != -1) {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()

                    for (index in studData.indices) {
                        val value = studData[index]
                        val insert = "INSERT INTO student (id, teacher_id, studGroup, fullName) VALUES " +
                                "(NULL, '${teacherID}','${value.group}','${value.fullName}');"
                        ps.execute(insert)
                    }
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                deleteFile()
                runOnUiThread { success() }
            }
        }
        @Suppress("DEPRECATION")
        private fun cellCheck(cell: Cell): String {
            var cellValue = ""
            when (cell.cachedFormulaResultType) {
                Cell.CELL_TYPE_NUMERIC ->  cellValue = String.format("%.0f", cell.numericCellValue)
                Cell.CELL_TYPE_STRING -> cellValue = cell.richStringCellValue.toString()
            }
            return cellValue
        }

        private fun startProcess() {
            binding.process.isVisible = true
            binding.linearLayout.isVisible = false
        }
        private fun success() {
            binding.process.isVisible = false
            binding.linearLayout.isVisible = false
            val mDialogSuccess = LayoutInflater.from(this).inflate(R.layout.success, null)
            val mBuilderSuccess = AlertDialog.Builder(this)
                .setView(mDialogSuccess).setTitle("Данные загружены")
            val windowSuccess = mBuilderSuccess.show()
            object : CountDownTimer(1800, 600){
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("tik", "tak")
                }
                override fun onFinish() {
                    windowSuccess.dismiss()
                    finish()
                }
            }.start()
        }


    }
