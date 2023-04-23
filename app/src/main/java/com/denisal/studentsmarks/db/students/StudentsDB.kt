package com.denisal.studentsmarks.db.students

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [StudentsDataRoom::class], version = 1)
abstract  class StudentsDB: RoomDatabase() {
    abstract fun getStudentsDao(): StudentsDao
    companion object {
        fun getDB(context: Context): StudentsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                StudentsDB::class.java,
                "studentsData"
            ).build()
        }
    }
}