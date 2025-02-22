package db

import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

class Transaction(private val db: DataSource) {

  companion object {
    private val currentTransaction = ThreadLocal<Transaction?>()

    fun current(): Transaction? = currentTransaction.get()
  }

  private var conn: Connection? = null

  val connection: Connection
    get() = conn ?: db.connection.also {
      it.autoCommit = false
      conn = it
    }

  fun close(commit: Boolean) {
    try {
      conn?.apply {
        if (commit) commit() else rollback()
        autoCommit = true
        close()
      }
    } finally {
      conn = null
      detach()
    }
  }

  fun attach() {
    currentTransaction.set(this)
  }

  private fun detach() {
    currentTransaction.set(null)
  }
}

fun <R> DataSource.withConnection(block: Connection.() -> R): R {
  val tx = Transaction.current()
  return try {
    if (tx != null) tx.connection.block()
    else connection.use(block)
  } catch (e: SQLException) {
    throw if (e.message?.contains("unique constraint") == true) AlreadyExistsException(e) else e
  }
}

