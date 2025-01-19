package db

import kotlinx.coroutines.ThreadContextElement
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

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

  fun detach() {
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

fun <R> DataSource.inTransaction(block: Connection.() -> R): R {
  val transaction = Transaction(this)
  transaction.attach() // Присоединяем транзакцию к текущему потоку
  try {
    val result = transaction.connection.block() // Выполняем блок в транзакции
    transaction.close(true) // Завершаем транзакцию с commit
    return result
  } catch (e: Exception) {
    transaction.close(false) // Завершаем транзакцию с rollback
    throw e // Пробрасываем исключение дальше
  }
}

class TransactionCoroutineContext(private val tx: Transaction? = Transaction.current()) :
  ThreadContextElement<Transaction?>, AbstractCoroutineContextElement(Key) {

  companion object Key : CoroutineContext.Key<TransactionCoroutineContext>

  override fun updateThreadContext(context: CoroutineContext): Transaction? {
    tx?.attach()
    return tx
  }

  override fun restoreThreadContext(context: CoroutineContext, oldState: Transaction?) {
    oldState?.attach()
  }
}