package com.denisal.studentsmarks


data class StudentsData(
    val id: Int,
    val group: String,
    val fullName: String,
)
data class SubjData(
    val id: Int,
    val name: String,
    val teachid: Int,
    val lecture: Int,
    val practic: Int,
)
data class LessonData(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val courseId: Int,
    val lessonType: String,
)
data class TaskData(
    val id: Int,
    val name: String,
    val courseId: Int,
    val lessonId: Int,
)
data class DataListCourseAndLesson(
    val posCourse: Int,
    val posLesson: Int,
    val idCourse: Int,
    val idLesson: Int,
)
data class TaskListFIOAndLesson(
    val posFIO: Int,
    val posMark: Int,
    val idFIO: Int,
    val idMark: Int,
    val taskName: String,
    val groupStud: String,
    val mark: String,
)
data class DataAssessments(
    val id: Int,
    val value: String,
    val date: String,
    val studentIdAssess: Int,
    val taskIdAssess: Int,
)
data class FullData(
    val id: Int,
    val value: String,
    val date: String,
    val studId: Int,
    val taskId: Int,
    val idTask: Int,
    val name: String,
    val courseId: Int,
    val lessonId: Int,
    val idStud: Int,
    val teacherID: Int,
    val studGroup: String,
    val fullName: String,
)
data class StudentsArray(
    val fio: String,
    val group: String,
)
data class arrayCourseLesson(
    val idLesson: Int,
    val nameLesson: String,
    val dateLesson: String,
    val timeLesson: String,
    val courseIDLesson: Int,
    val idCourse: Int,
    val nameCourse: String
)
var id_task = -1
var listStudent: MutableList<String> = arrayListOf()
var studentArrayCheck: MutableList<StudentsArray> = arrayListOf()
var filenameDelete = ""
val studData: MutableList<StudentsData> = mutableListOf()
var uid: String = "null"
var teacherID: Int = -1
