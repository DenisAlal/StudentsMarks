package com.denisal.studentsmarks.viewsgrade.viewmodels

data class TrafficDataView(
    val id: Int,
    val studentId: Int,
    val date: String,
    val courseName: String,
    val lessonName: String,
    val fio: String,
    val lessonID: Int,
)
