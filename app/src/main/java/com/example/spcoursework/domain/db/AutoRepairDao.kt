package com.example.spcoursework.domain.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus

@Dao
interface AutoRepairDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: Request)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("SELECT * FROM requests")
    fun getAllRequests(): LiveData<List<Request>>

    @Query("SELECT * FROM employees WHERE phoneNumber = :phoneNumber LIMIT 1")
    fun getEmployeeWithPhoneLD(phoneNumber: String): LiveData<Employee>

    @Query("SELECT * FROM employees WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getEmployeeWithPhone(phoneNumber: String): Employee

    @Transaction
    @Query("SELECT * FROM requests WHERE status = :requestStatus")
    suspend fun getRequestsByStatus(requestStatus: RequestStatus): List<Request>


    @Query("SELECT id FROM clients WHERE carNumber = :carNumber LIMIT 1")
    suspend fun getClientIdByVehicleNumber(carNumber: String): Int?

    @Transaction
    suspend fun insertRequestWithClientIdByCarNumber(carNumber: String, request: Request) {
        val clientId = getClientIdByVehicleNumber(carNumber)
        clientId?.let {
            val requestWithClientId = request.copy(clientId = it)
            insertRequest(requestWithClientId)
        }
    }
}