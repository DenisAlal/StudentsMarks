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

var id_task = -1
var listStudent: List<String> = listOf()
var filenameDelete = ""
val studData: MutableList<StudentsData> = mutableListOf()
var uid: String = "null"
var teacherID: Int = -1
