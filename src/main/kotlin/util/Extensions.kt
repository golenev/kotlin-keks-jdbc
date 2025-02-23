package util

import library_layer.MyColumnName
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

inline val Int.d get() = toBigDecimal()
inline val Double.d get() = toBigDecimal()
inline val String.d get() = toBigDecimal()
inline val String.c get() = Currency.getInstance(this)

inline fun <reified T : Any> KProperty1<T, *>.column(): String {
    return findAnnotation<MyColumnName>()?.value
        ?: throw IllegalArgumentException("MyColumnName annotation not found for property: ${this.name}")}
