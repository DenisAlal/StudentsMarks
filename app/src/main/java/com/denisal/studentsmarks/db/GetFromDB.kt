package com.denisal.studentsmarks.db

import android.util.Log
import com.denisal.studentsmarks.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class GetFromDB() {
    fun getTeacher() {
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM teacher WHERE uid = '$uid'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    teacherID = id
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
    }

    fun getDataCourse(): MutableList<SubjData> {
        val courseArray: MutableList<SubjData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM course WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val teachid = result.getInt(3)
                    val lecture = result.getInt(4)
                    val practic = result.getInt(5)
                    courseArray.add(SubjData(id, name, teachid, lecture, practic))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return courseArray
    }
    fun getStudentsData(idStudent: Int): MutableList<StudentsData> {
        val studentArray: MutableList<StudentsData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM student WHERE id = '$idStudent' AND teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val group = result.getString(3)
                    val fullName = result.getString(4)
                    studentArray.add(StudentsData(id, group, fullName))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return studentArray
    }
    fun getFullData(idCourse: Int, idLesson: Int): MutableList<FullData> {
        val studentArray: MutableList<FullData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM `assessment` INNER JOIN `task` ON task_id = task.id " +
                        "INNER JOIN `student` ON student_id = student.id WHERE task.course_id = $idCourse " +
                        "AND student.teacher_id = $teacherID AND task.lesson_id = $idLesson"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val value = result.getString(2)
                    val date = result.getString(3)
                    val studId = result.getInt(4)
                    val taskId = result.getInt(5)
                    val idTask = result.getInt(6)
                    val name = result.getString(7)
                    val courseId = result.getInt(8)
                    val lessonId = result.getInt(9)
                    val idStud = result.getInt(10)
                    val teacherID = result.getInt(11)
                    val studGroup = result.getString(12)
                    val fullName = result.getString(13)
                    studentArray.add(FullData(id,value, date, studId, taskId, idTask, name, courseId, lessonId, idStud, teacherID, studGroup, fullName))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return studentArray
    }
    fun getDataAssessments(taskID: Int): MutableList<DataAssessments> {
        val assessmentsArray: MutableList<DataAssessments> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM assessment WHERE task_id = '$taskID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val value = result.getString(2)
                    val date = result.getString(3)
                    val studentIdAssess = result.getInt(4)
                    val taskIdAssess = result.getInt(5)
                    assessmentsArray.add(DataAssessments(id, value, date, studentIdAssess, taskIdAssess))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return assessmentsArray
    }
    fun getDataLesson(): MutableList<LessonData> {
        val lessonArray: MutableList<LessonData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM lesson WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val date = result.getString(4)
                    val time = result.getString(5)
                    val courseId = result.getInt(6)
                    val lessonType = result.getString(7)
                    lessonArray.add(LessonData(id, name, date, time, courseId, lessonType))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return lessonArray
    }

    fun getArraySubjDuplicate(courseName: String): Boolean {
        var ret = false
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM course WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val name = result.getString(2)
                    if (courseName.toLowerCase() == name.toLowerCase()) {
                        ret = true
                    }
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return ret
    }

    fun getLesson(getID: Int): MutableList<LessonData> {
        val lessonArray: MutableList<LessonData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql = "SELECT * FROM lesson WHERE course_id = '$getID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val date = result.getString(4)
                    val time = result.getString(5)
                    val courseId = result.getInt(6)
                    val lessonType = result.getString(7)
                    lessonArray.add(LessonData(id, name, date, time, courseId, lessonType))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return lessonArray
    }

    fun getTasks(getIDCourse: Int, getIDLesson: Int): MutableList<TaskData> {
        val taskData: MutableList<TaskData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql =
                    "SELECT * FROM task WHERE course_id = '$getIDCourse' AND lesson_id = '$getIDLesson'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val courseId = result.getInt(3)
                    val lessonId = result.getInt(4)
                    taskData.add(TaskData(id, name, courseId, lessonId))
                }
                Log.e("data:", taskData.toString())
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return taskData
    }



    fun checkStudent(
        fio: String,
        group: String,
    ): MutableList<StudentsData> {
        val studentData: MutableList<StudentsData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql =
                    "SELECT * FROM student WHERE fullName = '$fio' AND " +
                            "studGroup = '$group' AND teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val groupStud = result.getString(3)
                    val fullName = result.getString(4)
                    studentData.add(StudentsData(id, groupStud, fullName))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return studentData
    }
    fun checkStudentQR(
    ): MutableList<StudentsData> {
        val studentData: MutableList<StudentsData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql =
                    "SELECT * FROM student WHERE teacher_id = '$teacherID'"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val groupStud = result.getString(3)
                    val fullName = result.getString(4)
                    studentData.add(StudentsData(id, groupStud, fullName))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return studentData
    }
    fun getLessonDateTime(name: String): MutableList<LessonData> {
        val lesson: MutableList<LessonData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql =
                    "SELECT * FROM lesson WHERE name = '$name' AND teacher_id = $teacherID"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val date = result.getString(4)
                    val time = result.getString(5)
                    val courseId = result.getInt(6)
                    val lessonType = result.getString(7)
                    lesson.add(LessonData(id, name, date, time, courseId, lessonType))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return lesson
    }
    fun getTraffic(lessonId: Int): MutableList<StudentsArray> {
        val trafficData: MutableList<StudentsArray> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val sql =
                    "SELECT student.studGroup, student.fullName FROM `traffic` INNER JOIN `student` ON student_id = student.id WHERE traffic.lesson_id = $lessonId"
                val query = cn.prepareStatement(sql)
                val result = query.executeQuery()
                while (result.next()) {
                    val groupStud = result.getString(1)
                    val fullName = result.getString(2)
                    trafficData.add(StudentsArray(fullName, groupStud ))
                }
                cn.close()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return trafficData
    }
}