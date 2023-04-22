package com.denisal.studentsmarks.db

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
        numberSubj: String,
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
                        "INSERT INTO `lesson`(`id`, `name`, `teacher_id`, `date`, `numberSubj`, " +
                                "`course_id`, `lessonType`) VALUES (NULL,'$name',$teacherID,'$date','$numberSubj',$course_id, '$selectedType')"
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
    fun insertTask(name: String, courseid: Int): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert =
                        "INSERT INTO `task`(`id`, `name`, `course_id`) VALUES " +
                                "(NULL,'$name',$courseid)"
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
    fun insertAssessment(value: String, date: String, studentIDAssessment: Int, taskIDAssessment: Int): Boolean {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert =
                        "INSERT INTO `assessment`(`id`, `value`, `date`, `student_id`, `task_id`) VALUES " +
                                "(NULL,'$value','$date', $studentIDAssessment, $taskIDAssessment)"
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
    fun insertTraffic(arrayInsert: MutableList<Int>, lessonID: Int): Boolean  {
        var ret = true
        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()

                    for (index in arrayInsert.indices) {
                        val insert = "INSERT INTO `traffic`(`id`, `student_id`, `lesson_id`) " +
                                "VALUES (NULL,'${arrayInsert[index]}','$lessonID')"
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