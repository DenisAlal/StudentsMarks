package com.denisal.studentsmarks.db.session

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.denisal.studentsmarks.StudentsData

@Database(entities = [StudentsData::class], version = 1)
abstract  class StudentsDB: RoomDatabase() {
    abstract fun getStudentsDao(): StudentsDao
    companion object {
        fun getDB(context: Context): StudentsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                StudentsDB::class.java,
                "sessionData"
            ).build()
        }
    }
}