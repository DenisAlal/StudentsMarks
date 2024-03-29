package com.denisal.studentsmarks.db
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class DeleteFromDb {
    fun deleteOneStudent(idStud: Int): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert = "DELETE FROM student WHERE id = $idStud"
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
    fun deleteMark(idMark: Int): Boolean {
        var ret = true

        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert = "DELETE FROM assessment WHERE id = $idMark"
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
    fun deleteOneCourse(idCourse: Int): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert = "DELETE FROM course WHERE id = $idCourse"
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
     fun deleteOneTask(idTask: Int): Boolean {
         var ret = true
         if (teacherID != -1) {
             thread {
                 try {
                     Class.forName("com.mysql.jdbc.Driver")
                     val cn: Connection = DriverManager.getConnection(url, user, pass)
                     val ps = cn.createStatement()
                     val insert = "DELETE FROM task WHERE id = $idTask"
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

    fun deleteInfo(deleteData: MutableList<Boolean>): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    if(deleteData[0]) {
                        val insert = "DELETE FROM student WHERE teacher_id='$teacherID'"
                        ps.execute(insert)
                    }
                    if(deleteData[1]) {
                        val insert = "DELETE FROM course WHERE teacher_id='$teacherID'"
                        ps.execute(insert)
                    }
                    if(deleteData[2]) {
                        val insert = "DELETE FROM lesson WHERE teacher_id='$teacherID'"
                        ps.execute(insert)
                    }

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