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
var filenameDelete = ""
val studData: MutableList<StudentsData> = mutableListOf()
var uid: String = "null"
var teacherID: Int = -1
