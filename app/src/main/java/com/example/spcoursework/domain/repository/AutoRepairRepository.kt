package com.example.spcoursework.domain.repository

import com.example.spcoursework.domain.db.AutoRepairDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoRepairRepository(
    private val autoRepairDB: AutoRepairDB
) {
    suspend fun getEmployeeWithPhone(phone: String) = withContext(Dispatchers.IO) {
        autoRepairDB.getDao().getEmployeeWithPhone(phone)
    }

    fun getEmployeeWithPhoneLD(phone: String) = autoRepairDB.getDao().getEmployeeWithPhoneLD(phone)

}