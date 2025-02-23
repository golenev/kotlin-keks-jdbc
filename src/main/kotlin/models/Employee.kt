package models

import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

// Наша аннотация
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class MyColumnName(val value: String)

data class Employee (
    @MyColumnName("employee_id")
    val employeeId: Long,
    @MyColumnName("employee_name")
    val employeeName: String,
    @MyColumnName("age")
    val age: Int,
    @MyColumnName("sex")
    val sex: String,
    @MyColumnName("department_name")
    val departmentId: Int,
)

// Extension-функция для KProperty, которая возвращает значение аннотации @JsonProperty
fun KProperty<*>.column(): String? {
    return this.findAnnotation<MyColumnName>()?.value
}

fun main() {
    // Получаем значение аннотации @JsonProperty для свойства employeeName
    val jsonPropertyValue = Employee::employeeName.column()
    println("JsonProperty value: $jsonPropertyValue") // Вывод: employee_name
}