// Database.kt
package utils

import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val DB_URL = "jdbc:mysql://localhost:3306/MARYKAY_REPORTS"
    private const val DB_USER = "root"
    private const val DB_PASSWORD = "Qwerty123456"

    fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}