package com.denisal.studentsmarks.dbfunctions

import android.text.Editable
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
    fun insertLeson(
        name: String,
        date: String,
        time: String,
        course_id: Int,
        selectedType: String
    ): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert =
                        "INSERT INTO `lesson`(`id`, `name`, `teacher_id`, `date`, `time`, `course_id`, `lessonType`) VALUES (NULL,'$name',$teacherID,'$date','$time',$course_id, '$selectedType')"
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