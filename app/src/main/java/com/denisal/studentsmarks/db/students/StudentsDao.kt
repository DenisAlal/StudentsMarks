package com.denisal.studentsmarks.db.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentsDao {
    @Insert
    fun insertStudent(students: StudentsData)
    @Query("SELECT * FROM students")
    fun getStudents(): Flow<List<StudentsData>>
    @Query("DELETE FROM students")
    fun deleteStudents()
    @Query("DELETE FROM students WHERE id = :id")
    fun deleteOneStudent(id : Int):Int
    @Query("DELETE FROM sqlite_sequence WHERE name = 'students';")
    fun resetAutoIncrementValueStudents()
    @Query("SELECT * FROM students")
    fun loadAllStudents(): MutableList<StudentsData?>?
}