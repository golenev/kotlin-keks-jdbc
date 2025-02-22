package db

import java.sql.ResultSet
import javax.sql.DataSource

abstract class BaseRepository(protected val table: String) {
    protected val db: DataSource
        get() = MyDbConnection.getDataSource()

    fun <R> fullJoin(
        otherTable: String, // Вторая таблица
        joinCondition: String, // Условие JOIN (например, "d.department_id = e.department_id")
        where: Map<String, Any?> = emptyMap(), // Условия WHERE (например, mapOf("e.age" to 48))
        suffix: String = "", // Дополнительные параметры (ORDER BY, LIMIT и т.д.)
        mapper: ResultSet.() -> R // Маппер для преобразования ResultSet в объект
    ): List<R> {
        val mainTableAlias = table[0]
        val otherTableAlias = otherTable[0]
        val sql = """
        SELECT *
        FROM $table $mainTableAlias
        FULL JOIN $otherTable $otherTableAlias ON $joinCondition
        $suffix
    """.trimIndent()
        return db.select(sql, where, mapper = mapper)
    }
}