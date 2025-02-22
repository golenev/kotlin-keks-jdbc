package db

import org.postgresql.ds.PGSimpleDataSource
import java.sql.Connection
import javax.sql.DataSource

object MyDbConnection {

    private val dataSource: DataSource = PGSimpleDataSource().apply {
        setURL("jdbc:postgresql://localhost:34567/mydatabase")
        user = "myuser"
        password = "mypassword"
    }

    fun <R> inTransaction(block: Connection.() -> R): R {
        val connection = dataSource.connection
        connection.autoCommit = false
        try {
            val result = block(connection)
            connection.commit()
            return result
        } catch (e: Exception) {
            connection.rollback()
            throw e
        } finally {
            connection.close()
        }
    }

    fun getDataSource(): DataSource = dataSource

}







