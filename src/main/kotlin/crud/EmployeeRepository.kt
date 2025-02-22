package crud

import db.*
import models.Employee
import models.EmployeeWithDepartment
import java.sql.ResultSet

object EmployeeRepository : BaseRepository(table = "Employee") {

    private val selectMapper: ResultSet.() -> Employee = {
        Employee(
            employeeId = getLong("employee_id"),
            employeeName = getString("employee_name"),
            age = getInt("age"),
            sex = getString("sex"),
            departmentId = getInt("department_id")
        )
    }

    private val joinMapper: ResultSet.() -> EmployeeWithDepartment = {
        EmployeeWithDepartment(
            employeeId = getLong("employee_id"),
            employeeName = getString("employee_name"),
            departmentId = getLong("department_id"),
            departmentName = getString("department_name"),
            employeeAge = getString("age")
        )
    }

    fun getEmployeesWithDepartmentsByEmpId(employeeId: Long): List<EmployeeWithDepartment> {
        return fullJoin(
            otherTable = "Department", // Вторая таблица
            joinCondition = "d.department_id = e.department_id", // Условие JOIN
            where = where { "e.employee_id" equals employeeId }, // Условие WHERE читать как where e.employee_id = 'employeeId'
            mapper = joinMapper
        )
    }

    fun getEmployeeById(id: Long): Employee? =
        db.query(table, where = where { "employee_id" equals id }, mapper = selectMapper).firstOrNull()

    fun createEmployee(employee: Employee) {
        db.insert(
            table, mapOf(
                "name" to employee.employeeName,
                "age" to employee.age,
                "sex" to employee.sex,
                "department_id" to employee.departmentId
            )
        )
    }

    fun updateEmployee(id: Long, name: String, age: Int, sex: String, departmentId: Long): Int =
        db.update(
            table, mapOf("id" to id), mapOf(
                "name" to name,
                "age" to age,
                "sex" to sex,
                "department_id" to departmentId
            )
        )

    fun deleteEmployee(id: Long): Int =
        db.delete(table, mapOf("id" to id))
}