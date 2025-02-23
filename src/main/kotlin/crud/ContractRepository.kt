package crud

import library_layer.*
import models.Contract
import util.column
import java.sql.ResultSet

object ContractRepository : BaseRepository(table = "Contracts") {

    private val contractMapper: ResultSet.() -> Contract = {
         Contract(
            id = getLong(Contract::id.column()),
            employeeId = getLong(Contract::employeeId.column()),
            birthDate = getLocalDate(Contract::birthDate.column()),
            contractStartDate = getLocalDateTime(Contract::contractStartDate.column()),
            contractEndDate = getLocalDateTime(Contract::contractEndDate.column())
        )
    }

    fun getContractByEmpId(empId: Long): Contract? =
        db.query(
            table,
            where = where { Contract::employeeId.column() equals empId },
            mapper =  contractMapper
        ).firstOrNull()

}