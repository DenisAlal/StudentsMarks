package com.denisal.studentsmarks.dbfunctions
import com.denisal.studentsmarks.pass
import com.denisal.studentsmarks.teacherID
import com.denisal.studentsmarks.url
import com.denisal.studentsmarks.user
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class DeleteFromDb {
    fun deleteOneStudent() {

    }
    fun deleteMark(idMark: Int): Boolean {
        var ret = true

        if (teacherID != -1) {
            thread {
                try {
                    Class.forName("com.mysql.jdbc.Driver")
                    val cn: Connection = DriverManager.getConnection(url, user, pass)
                    val ps = cn.createStatement()
                    val insert = "DELETE FROM assessment WHERE id = $idMark"
                    ps.execute(insert)
                    ps?.close()
                    cn.close()
                } catch (e: ClassNotFoundException) {
                    ret = false
                    e.printStackTrace()
                } catch (e: SQLException) {
                    ret = false
                    e.printStackTrace()
                }
            }.join()
        } else {
            ret = false
        }
        return ret
    }
}