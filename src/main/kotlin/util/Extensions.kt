package util

import library_layer.MyColumnName
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

inline val Int.d get() = toBigDecimal()
inline val Double.d get() = toBigDecimal()
inline val String.d get() = toBigDecimal()
inline val String.c get() = Currency.getInstance(this)
fun KProperty<*>.column(): String {
    return this.findAnnotation<MyColumnName>()?.value ?: throw IllegalStateException("argument is null")
}
