package com.example.spcoursework.domain.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.Request
import java.util.UUID

@Dao
interface AutoRepairDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: Request)

    @Update
    suspend fun updateRequest(request: Request)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("SELECT * FROM requests")
    fun getAllRequests(): LiveData<List<Request>>

    @Query("SELECT * FROM employees WHERE phoneNumber = :phoneNumber LIMIT 1")
    fun getEmployeeWithPhoneLD(phoneNumber: String): LiveData<Employee>

    @Query("SELECT * FROM employees WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getEmployeeWithPhone(phoneNumber: String): Employee

    @Query("SELECT * FROM clients WHERE carNumber = :carNumber LIMIT 1")
    suspend fun getClientWithCarNumber(carNumber: String): Client

    @Query("SELECT * FROM requests WHERE id = :requestId")
    suspend fun getRequestWithId(requestId: Int): Request
}