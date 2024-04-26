package com.example.spcoursework.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Request

data class ClientWithRequests(
    @Embedded val client: Client,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val requests: List<Request>
)
