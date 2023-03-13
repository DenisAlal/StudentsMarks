package com.denisal.studentsmarks.dbfunctions

import android.util.Log
import android.widget.Toast
import com.denisal.studentsmarks.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class InsertToDB {
    fun insertOneStudent( fio: String, group: String): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                        val insert =
                            "INSERT INTO student (id, teacher_id, studGroup, fullName) VALUES " +
                                    "(NULL, '$teacherID','$group','$fio');"
                        ps.execute(insert)
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    ret = false
                    e.printStackTrace()
                } catch (e: SQLException) {
                    ret = false
                    e.printStackTrace()
                }
            }.join()
        } else {
            ret = false
        }
        return ret
    }
    fun insertCourse(subName: String, lecture: Int, practic: Int): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert =
                        "INSERT INTO course (id, name, teacher_id, lecture, practic) VALUES " +
                                "(NULL, '$subName','$teacherID','$lecture', '$practic');"
                    ps.execute(insert)
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    ret = false
                    e.printStackTrace()
                } catch (e: SQLException) {
                    ret = false
                    e.printStackTrace()
                }
            }.join()
        } else {
            ret = false
        }
        return ret
    }
}