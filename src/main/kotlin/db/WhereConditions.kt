package db

data class Condition(val key: String, val operator: String, val value: Any?)

infix fun String.equals(value: Any?): Condition = Condition(this, "=", value)

fun where(builder: () -> Condition): Map<String, Any?> {
    val condition = builder()
    return mapOf(condition.key to condition.value)
}