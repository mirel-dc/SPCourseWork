package com.example.spcoursework.domain.network

import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface AutoRepairService {
    @GET("employees")
    suspend fun getEmployees(): Response<List<Employee>>

    @GET("clients")
    suspend fun getClients(): Response<List<Client>>

    @PUT("clients")
    suspend fun inputClient(client: Client): Response<Unit>
}