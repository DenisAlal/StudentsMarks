package com.denisal.studentsmarks.db.assessments

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessDao {
    @Insert
    fun insertAssess(assess: AssesDataRoom)
    @Query("SELECT * FROM assessment")
    fun getAssess(): Flow<List<AssesDataRoom>>
    @Query("SELECT * FROM assessment WHERE student_id = :id")
    fun getAssessByID(id: Int): Flow<List<AssesDataRoom>>
    @Query("DELETE FROM assessment")
    fun deleteAssess()
    @Query("DELETE FROM assessment WHERE id = :id")
    fun deleteOneAssess(id : Int):Int
    @Query("DELETE FROM sqlite_sequence WHERE name = 'assessment';")
    fun resetAutoIncrementValueAssess()
    @Query("SELECT * FROM assessment")
    fun loadAllAssess(): MutableList<AssesDataRoom>
}