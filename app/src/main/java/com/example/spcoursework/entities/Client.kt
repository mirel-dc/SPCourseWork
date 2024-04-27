package com.example.spcoursework.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = false)
    val phoneNumber: String,
    val name: String,
    val carNumber: String
)
