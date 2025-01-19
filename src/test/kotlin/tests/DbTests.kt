package tests

import crud.EmployeeRepository
import db.DBModule
import models.Employee
import org.junit.jupiter.api.Test

class DbTests {


    @Test
    fun keksTest () {
        println("qwerqwer")
        // Настройка подключения к базе данных
        val dataSource = DBModule.configure()

        // Создание репозитория
        val employeeRepository = EmployeeRepository(dataSource)

        // Создание нового сотрудника
        val newEmployee: Employee = employeeRepository.createEmployee("John Doe", 30, "Male", 1, "Finance")
        println("Created employee: $newEmployee")
/*
        // Получение сотрудника по ID
        val employee = employeeRepository.getEmployeeById(newEmployee.id)
        println("Employee: $employee")

        // Обновление сотрудника
        val updatedRows = employeeRepository.updateEmployee(newEmployee.id, "Jane Doe", 31, "Female", 2)
        println("Updated $updatedRows rows")

        // Удаление сотрудника
        val deletedRows = employeeRepository.deleteEmployee(newEmployee.id)
        println("Deleted $deletedRows rows")*/
    }
}