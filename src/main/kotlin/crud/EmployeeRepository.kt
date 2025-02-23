package crud

import library_layer.*
import models.Employee
import models.EmployeeWithDepartment
import util.column
import java.sql.ResultSet

object EmployeeRepository : BaseRepository(table = "Employee") {

    private val selectMapper: ResultSet.() -> Employee = {
        Employee(
            employeeId = getLong(Employee::employeeId.column()),
            employeeName = getString(Employee::employeeName.column()),
            age = getInt(Employee::age.column()),
            sex = getString(Employee::sex.column()),
            departmentId = getInt(Employee::departmentId.column())
        )
    }

    private val joinMapper: ResultSet.() -> EmployeeWithDepartment = {
        EmployeeWithDepartment(
            employeeId = getLong(EmployeeWithDepartment::employeeId.column()),
            employeeName = getString(EmployeeWithDepartment::employeeName.column()),
            departmentId = getLong(EmployeeWithDepartment::departmentId.column()),
            departmentName = getString(EmployeeWithDepartment::departmentName.column()),
            age = getString(EmployeeWithDepartment::age.column()),
            sex = getString(EmployeeWithDepartment::sex.column())
        )
    }

    fun getEmployeesWithDepartmentsByEmpId(employeeId: Long): List<EmployeeWithDepartment> =
        fullJoin(
            otherTable = "Department", // Вторая таблица
            joinCondition = "d.department_id = e.department_id", // Условие JOIN, тут подумать, как убрать хардкод
            where = where { "e.${Employee::employeeId.column()}" equals employeeId }, // Условие WHERE читать как where e.employee_id = 'employeeId'
            mapper = joinMapper
        )

    fun getEmployeeById(id: Long): Employee? =
        db.query(
            table,
            where = where { Employee::employeeId.column() equals id },
            mapper = selectMapper
        ).firstOrNull()

    fun createEmployee(employee: Employee) =
        db.insert(
            table,
            values = values {
                "employee_id" to employee.employeeId
                "employee_name" to employee.employeeName
                "age" to employee.age
                "sex" to employee.sex
                "department_id" to employee.departmentId
            }
        )

    fun deleteEmployee(id: Long): Int =
        db.delete(table, where { Employee::employeeId.column() equals id })
}