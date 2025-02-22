package tests

import crud.EmployeeRepository
import db.MyDbConnection
import models.Employee
import org.junit.jupiter.api.Test
import util.FakeUtils

class DbTests {

    @Test
    fun testCreateEmp() {

        val employee = Employee(
            employeeId = 0,
            employeeName = FakeUtils.faker.name().name(),
            age = FakeUtils.faker.number().numberBetween(18, 67),
            sex = "male",
            departmentId = 101
        )
        MyDbConnection.inTransaction {
            EmployeeRepository.createEmployee(employee)
        }
    }

    @Test
    fun selectWhereTest() {
        val res = MyDbConnection.inTransaction { EmployeeRepository.getEmployeeById(25) }
        val r = 0
    }

    @Test
    fun joinWithWhereTest() {
        val res = MyDbConnection.inTransaction { EmployeeRepository.getEmployeesWithDepartmentsByEmpId(25) }
        val r = 0
    }

}