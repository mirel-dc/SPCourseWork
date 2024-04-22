package com.example.spcoursework.data.models

data class Request(
    val id: Int,
    val clientId: Int,
    val workerId : Int?,
    val carNumber: String,
    val problemDescription: String?,
    val status : RequestStatus
)

enum class RequestStatus(){
    ARRIVED,
    WORKING,
    FINISHED
}
