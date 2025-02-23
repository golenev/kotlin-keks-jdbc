package models

import library_layer.MyColumnName
import java.time.LocalDate
import java.time.LocalDateTime

data class Contract(
    @MyColumnName("id") val id: Long,
    @MyColumnName("employee_id") val employeeId: Long,
    @MyColumnName("birth_date") val birthDate: LocalDate,
    @MyColumnName("contract_start_date") val contractStartDate: LocalDateTime,
    @MyColumnName("contract_end_date") val contractEndDate: LocalDateTime
)

