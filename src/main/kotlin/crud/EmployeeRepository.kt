package crud

import db.*
import models.Department
import models.Employee
import java.sql.ResultSet
import java.util.*
import javax.sql.DataSource

class EmployeeRepository(db: DataSource) : BaseRepository(db, "Employee") {
    private val mapper: ResultSet.() -> Employee = {
        Employee(
            id = getLong("id"),
            name = getString("name"),
            age = getInt("age"),
            sex = getString("sex"),
            department = Department(getLong("department_id"), getString("name"))
        )
    }

    // Получение сотрудника по ID
    fun getEmployeeById(id: Long): Employee? =
        db.query(table, mapOf("id" to id), mapper = mapper).firstOrNull()

    // Создание нового сотрудника
    fun createEmployee(name: String, age: Int, sex: String, departmentId: Long, departmentName: String): Employee {
        // Проверяем, существует ли Department с указанным id
//        val departmentExists = db.query("Department", mapOf("id" to departmentId)) {
//            getLong("id")
//        }.isNotEmpty()
//
//        if (!departmentExists) {
//            throw IllegalArgumentException("Department with id $departmentId does not exist")
//        }

        // Создаем сотрудника
        val employee = Employee(
            id = 0, // ID будет сгенерирован базой данных
            name = name,
            age = age,
            sex = sex,
            department = Department(departmentId, departmentName)
        )

        // Вставляем запись в таблицу Employee

        db.inTransaction {
            db.insert(
                "Department", mapOf(
                    "id" to departmentId,
                    "name" to departmentName
                )
            )
            db.insert(
                table, mapOf(
                    "name" to employee.name,
                    "age" to employee.age,
                    "sex" to employee.sex,
                    "department_id" to employee.department.id
                )
            )
        }
        return employee
    }

    // Обновление сотрудника
    fun updateEmployee(id: Long, name: String, age: Int, sex: String, departmentId: Long): Int =
        db.update(
            table, mapOf("id" to id), mapOf(
                "name" to name,
                "age" to age,
                "sex" to sex,
                "department_id" to departmentId
            )
        )

    // Удаление сотрудника
    fun deleteEmployee(id: Long): Int =
        db.delete(table, mapOf("id" to id))
}