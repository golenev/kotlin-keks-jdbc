package models

import library_layer.MyColumnName

data class Employee (
    @MyColumnName("employee_id")
    val employeeId: Long,
    @MyColumnName("employee_name")
    val employeeName: String,
    @MyColumnName("age")
    val age: Int,
    @MyColumnName("sex")
    val sex: String,
    @MyColumnName("department_id")
    val departmentId: Int,
)


