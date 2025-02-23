package library_layer

import org.intellij.lang.annotations.Language
import java.net.URL
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneOffset.UTC
import java.util.*
import javax.sql.DataSource

fun <R> DataSource.query(
    table: String,
    where: Map<String, Any?>,
    suffix: String = "",
    mapper: ResultSet.() -> R,
): List<R> =
    select("select * from $table", where, suffix, mapper)

fun <R> DataSource.select(
    @Language("SQL") select: String,
    where: Map<String, Any?>,
    suffix: String = "",
    mapper: ResultSet.() -> R,
): List<R> = withConnection {
    val expr = "$select${whereExpr(where)} $suffix"
    prepareStatement(expr).use { stmt ->
        stmt.setAll(whereValues(where))
        stmt.executeQuery().map(mapper)
    }
}

fun DataSource.exec(@Language("SQL") expr: String, values: Sequence<Any?> = emptySequence()): Int = withConnection {
    prepareStatement(expr).use { stmt ->
        stmt.setAll(values)
        stmt.executeUpdate()
    }
}

fun DataSource.insert(table: String, values: Map<String, *>): Int =
    exec(insertExpr(table, values), setValues(values))

private fun insertExpr(table: String, values: Map<String, *>) = """
  insert into $table (${values.keys.joinToString()})
    values (${values.entries.joinToString { (it.value as? SqlExpr)?.expr(it.key) ?: "?" }})""".trimIndent()

fun DataSource.update(table: String, where: Map<String, Any?>, values: Map<String, *>): Int =
    exec("update $table set ${setExpr(values)}${whereExpr(where)}", setValues(values) + whereValues(where))

fun DataSource.delete(table: String, where: Map<String, Any?>): Int =
    exec("delete from $table${whereExpr(where)}", whereValues(where))

private fun setExpr(values: Map<String, *>) =
    values.entries.joinToString { it.key + " = " + ((it.value as? SqlExpr)?.expr(it.key) ?: "?") }

fun whereExpr(where: Map<String, Any?>) = if (where.isEmpty()) "" else " where " +
        where.entries.joinToString(" and ") { (k, v) -> whereExpr(k, v) }

private fun whereExpr(k: String, v: Any?) = when (v) {
    null -> "$k is null"
    is SqlExpr -> v.expr(k)
    is Iterable<*> -> inExpr(k, v)
    is Array<*> -> inExpr(k, v.toList())
    else -> "$k = ?"
}

private fun inExpr(k: String, v: Iterable<*>) = "$k in (${v.joinToString { "?" }})"

private fun setValues(values: Map<String, Any?>) = values.values.asSequence().flatMap { it.flatExpr() }

private fun Any?.flatExpr(): Iterable<Any?> = if (this is SqlExpr) values else listOf(this)

private fun whereValues(where: Map<String, Any?>) =
    where.values.asSequence().filterNotNull().flatMap { it.toIterable() }

private fun Any?.toIterable(): Iterable<Any?> = when (this) {
    is Array<*> -> toList()
    is Iterable<*> -> this
    else -> flatExpr()
}

operator fun PreparedStatement.set(i: Int, value: Any?) = setObject(i, connection.toDBType(value))
fun PreparedStatement.setAll(values: Sequence<Any?>) = values.forEachIndexed { i, v -> this[i + 1] = v }

private fun Connection.toDBType(v: Any?): Any? = when (v) {
    is Enum<*> -> v.name
    is Instant -> v.atOffset(UTC)
    is Period, is URL, is Currency -> v.toString()
    is Collection<*> -> createArrayOf(if (v.firstOrNull() is UUID) "uuid" else "varchar", v.toTypedArray())
    else -> v
}

private fun <R> ResultSet.map(mapper: ResultSet.() -> R): List<R> = mutableListOf<R>().also {
    while (next()) it += mapper()
}

// Экстеншен-функция для преобразования java.sql.Date в java.time.LocalDate
fun ResultSet.getLocalDate(column: String): LocalDate = getDate(column).toLocalDate()

// Экстеншен-функция для преобразования java.sql.Timestamp в java.time.LocalDateTime
fun ResultSet.getLocalDateTime(column: String): LocalDateTime = getTimestamp(column).toLocalDateTime()

open class SqlExpr(@Language("SQL") protected val expr: String, val values: Collection<*> = emptyList<Any>()) {
    constructor(expr: String, vararg values: Any?) : this(expr, values.toList())
    open fun expr(key: String) = expr
}

open class SqlOp(val operator: String, value: Any? = null) :
    SqlExpr(operator, if (value != null) listOf(value) else emptyList()) {
    override fun expr(key: String) = "$key $operator" + ("?".takeIf { values.isNotEmpty() } ?: "")
}

class NotIn(values: Collection<*>) : SqlExpr("", values) {
    constructor(vararg values: Any?) : this(values.toList())
    override fun expr(key: String) = inExpr(key, values).replace(" in ", " not in ")
}
