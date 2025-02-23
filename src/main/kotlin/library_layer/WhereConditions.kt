package library_layer

data class WhereCondition(val key: String, val operator: String, val value: Any?)

infix fun String.equals(value: Any?): WhereCondition = WhereCondition(this, "=", value)

fun where(builder: () -> WhereCondition): Map<String, Any?> {
    val condition = builder()
    return mapOf(condition.key to condition.value)
}