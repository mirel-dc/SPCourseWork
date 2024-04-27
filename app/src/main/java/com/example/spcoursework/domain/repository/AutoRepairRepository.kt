package com.example.spcoursework.domain.repository

import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoRepairRepository(
    private val autoRepairDB: AutoRepairDB
) {
    suspend fun getEmployeeWithPhone(phone: String) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().getEmployeeWithPhone(phone)
    }

    fun getEmployeeWithPhoneLD(phone: String) = autoRepairDB.getDao().getEmployeeWithPhoneLD(phone)

    fun getAllRequests() = autoRepairDB.getDao().getAllRequests()

    suspend fun insertEmployee(employee: Employee) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().insertEmployee(employee)
    }

    suspend fun insertRequest(request: Request) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().insertRequest(request)
    }


    suspend fun getClientWithCarNumber(carNumber: String?) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().getClientWithCarNumber(carNumber.toString().uppercase())
    }

    suspend fun insertClient(client: Client) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().insertClient(client)
    }
}