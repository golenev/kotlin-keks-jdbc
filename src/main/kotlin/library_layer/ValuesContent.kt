package library_layer

class ValuesBuilder {
    private val data = mutableMapOf<String, Any?>()
    infix fun String.to(value: Any?) {
        data[this] = value
    }

    fun build(): Map<String, Any?> = data
}

fun values(builder: ValuesBuilder.() -> Unit): Map<String, Any?> {
    val valuesBuilder = ValuesBuilder()
    valuesBuilder.builder()
    return valuesBuilder.build()
}
