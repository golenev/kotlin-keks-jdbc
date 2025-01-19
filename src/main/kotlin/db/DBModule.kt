package db

import jdk.internal.net.http.frame.DataFrame
import org.postgresql.ds.PGSimpleDataSource
import java.sql.Connection
import java.sql.SQLException


class DBModule {
    companion object {
        const val dbName = "mydatabase"

        fun configure(): PGSimpleDataSource {
            val dataSource = PGSimpleDataSource()
            dataSource.setURL("jdbc:postgresql://localhost:34567/$dbName")
            dataSource.user = "myuser"
            dataSource.password = "mypassword"
            return dataSource
        }
    }
}

abstract class AbstractPGConnection {
    private val dataSource by lazy { config() }

    abstract fun config(): PGSimpleDataSource

    @Throws(SQLException::class)
    fun withConnection(block: Connection.() -> Unit) {
        dataSource.connection.use(block)
    }
}





