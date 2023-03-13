package com.denisal.studentsmarks.dbfunctions

import android.util.Log
import com.denisal.studentsmarks.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class GetFromDB() {
    fun get() {
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts = "SELECT * FROM teacher WHERE uid = '$uid'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val UniqId = result.getString(2)
                    teacherID = id
                }

                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
    }
    fun getDataForSpinner(): MutableList<SubjData> {
        val subjData: MutableList<SubjData> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts = "SELECT * FROM course WHERE teacher_id = '$teacherID'"
                val searchAccountsQuery = cn.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()){
                    val id = result.getInt(1)
                    val name = result.getString(2)
                    val teachid = result.getInt(3)
                    val lecture = result.getInt(4)
                    val practic = result.getInt(5)
                    Log.e("data:", "$id $name $teachid $lecture $practic")
                }
                if (cn != null) {
                    cn.close()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.join()
        return subjData
    }
}