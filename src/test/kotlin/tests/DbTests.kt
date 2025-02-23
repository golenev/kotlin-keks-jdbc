package tests

import connection.MyDbConnection
import crud.ContractRepository.getContractByEmpId
import crud.EmployeeRepository.createEmployee
import crud.EmployeeRepository.deleteEmployee
import crud.EmployeeRepository.getEmployeeById
import crud.EmployeeRepository.getEmployeesWithDepartmentsByEmpId
import models.Employee
import org.junit.jupiter.api.Test
import util.FakeUtils

class DbTests {

    @Test
    fun selectWhereTest() {
        val res = MyDbConnection.inTransaction { getEmployeeById(25) }
        println(res)
    }

    @Test
    fun joinWithWhereTest() {
        val res = MyDbConnection.inTransaction { getEmployeesWithDepartmentsByEmpId(25) }
       println(res)
    }

    @Test
    fun deleteWhereTest() {
        val employee = Employee(
            employeeId = FakeUtils.faker.number().randomNumber(),
            employeeName = FakeUtils.faker.name().name(),
            age = FakeUtils.faker.number().numberBetween(18, 67),
            sex = "male",
            departmentId = 101
        )
        MyDbConnection.inTransaction {
            createEmployee(employee)
            deleteEmployee(employee.employeeId)
        }
    }

    @Test
    fun localDateTimeExtensionFunTest () {
       val res = MyDbConnection.inTransaction {
            getContractByEmpId(26)
        }
        println(res)
    }

}