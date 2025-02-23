package models

import library_layer.MyColumnName


data class EmployeeWithDepartment (
    @MyColumnName("employee_id")
    val employeeId: Long,
    @MyColumnName("employee_name")
    val employeeName: String,
    @MyColumnName("age")
    val age: String,
    @MyColumnName("department_id")
    val departmentId: Long,
    @MyColumnName("department_name")
    val departmentName: String,
    @MyColumnName("sex")
    val sex: String
)

