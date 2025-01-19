package models

data class Employee(

    val id: Long,

    val name: String,

    val age: Int,

    val sex: String,

    val department: Department
)

class Department(val id: Long, val name: String)