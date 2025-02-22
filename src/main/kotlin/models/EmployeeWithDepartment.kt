package models

data class EmployeeWithDepartment (
    val employeeId: Long,
    val employeeName: String,
    val employeeAge: String,
    val departmentId: Long,
    val departmentName: String
)
