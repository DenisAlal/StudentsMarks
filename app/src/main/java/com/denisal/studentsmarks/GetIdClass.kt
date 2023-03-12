package com.denisal.studentsmarks

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class GetIdClass() {
    fun get() {
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val cn: Connection = DriverManager.getConnection(url, user, pass)
                val searchAccounts = "SELECT * FROM teacher WHERE uid = '${uid}'"
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
}