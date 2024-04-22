package com.example.spcoursework.data.models

data class Workers(
    val id: Int,
    val name: String,
    val role: WorkerRoles,
    val phoneNumber: Number,
    val password : String
)

enum class WorkerRoles(){
    ADMIN,
    WORKER
}
