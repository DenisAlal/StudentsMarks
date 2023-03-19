package com.denisal.studentsmarks.dbfunctions

import android.util.Log
import com.denisal.studentsmarks.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class GetFromDB() {
    fun get() {
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM teacher WHERE uid = '$uid'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val uniqId = result.getString(2)
                    teacherID = id
                }

                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
    }
    fun getDataForSpinner(): MutableList<SubjData> {
        val subjData: MutableList<SubjData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM course WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val teachid = result.getInt(3)
                    val lecture = result.getInt(4)
                    val practic = result.getInt(5)
                    subjData.add(SubjData(id,name,teachid,lecture,practic))
                    Log.e("data:", "$id $name $teachid $lecture $practic")
                }
                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return subjData
    }
    fun getArraySubjDuplicate(courseName: String):Boolean {
        var ret = false
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM course WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()){
                    val name = result.getString(2)
                    if(courseName.toLowerCase() == name.toLowerCase()) {
                        ret = true
                    }
                }
                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return ret
    }
    fun getLesson(getID: Int): MutableList<LessonData> {
        val subjData: MutableList<LessonData> = mutableListOf()
        thread {
            try {
                //сощздать правитльный data class, посмротреть структуру в бд
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM lesson WHERE course_id = '$getID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val date= result.getString(4)
                    val time= result.getString(5)
                    val courseId= result.getInt(6)
                    val lessonType= result.getString(7)
                    subjData.add(LessonData(id,name,date,time,courseId,lessonType ))
                    Log.e("data:", "$id $name $date $time $courseId $lessonType")
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return subjData
    }
}