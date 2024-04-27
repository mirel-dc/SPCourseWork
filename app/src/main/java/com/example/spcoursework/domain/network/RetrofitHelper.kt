//package com.example.spcoursework.domain.network
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitHelper {
//    private const val BASE_URL = "http://10.0.2.2:3000/"
//
//    fun getInstance(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//    }
//}