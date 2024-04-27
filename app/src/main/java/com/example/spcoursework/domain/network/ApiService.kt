//package com.example.spcoursework.domain.network
//
//import android.util.Log
//import com.example.spcoursework.entities.Client
//import com.example.spcoursework.entities.Employee
//import kotlinx.coroutines.delay
//import retrofit2.Response
//import retrofit2.create
//
//const val TAG = "API_SERVICE"
//
//class ApiService{
//    private val service : AutoRepairService = RetrofitHelper.getInstance().create()
//
//    suspend fun getEmployees() : List<Employee>?{
//        return repeatTask { service.getEmployees() }
//    }
//
//    suspend fun inputClient(client: Client){
//        service.inputClient(client)
//    }
//
//
//    suspend fun getClients(): List<Client>? {
//        return repeatTask { service.getClients() }
//    }
//
//    private suspend fun <T> repeatTask(request: suspend () -> Response<T>): T? {
//        val maxTries = 5
//        var tries = 0
//        var isSuccess = false
//        var result: T? = null
//        while (!isSuccess && tries < maxTries) {
//            try {
//                val response = request()
//                if (response.isSuccessful) {
//                    result = response.body()!!
//                } else {
//                    Log.e(TAG, response.message())
//                    break
//                }
//                Log.d(TAG, "Задача выполнена, попыток: $tries")
//                isSuccess = true
//            } catch (e: Exception) {
//                Log.d(TAG, "Задача не выполнена, попытка: $tries", e)
//                tries++
//                delay(2000)
//            }
//        }
//        if (!isSuccess) {
//            Log.e(TAG, "Задача не выполнена, попыток: $tries")
//        }
//        return result
//    }
//
//}