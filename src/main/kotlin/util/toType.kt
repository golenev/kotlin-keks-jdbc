package util

import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.jvm.jvmErasure

fun String.toType(type: KType) = toType(if (Iterable::class.isSuperclassOf(type.jvmErasure)) type.arguments[0].type!!.jvmErasure else type.jvmErasure)

fun String.toType(type: KClass<*>) = when {
  type == Boolean::class -> toBoolean()
  type == Int::class -> toInt()
  type == BigDecimal::class -> d
  Enum::class.isSuperclassOf(type) -> type.java.enumConstants.find { (it as Enum<*>).name == this }
  else -> error("Unsupported type=$type")
}
