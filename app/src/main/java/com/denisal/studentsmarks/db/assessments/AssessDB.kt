package com.denisal.studentsmarks.db.assessments

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AssesDataRoom::class], version = 1)
abstract  class AssessDB: RoomDatabase() {
    abstract fun getAssesDao(): AssessDao
    companion object {
        fun getDB(context: Context): AssessDB {
            return Room.databaseBuilder(
                context.applicationContext,
                AssessDB::class.java,
                "assesData"
            ).build()
        }
    }
}