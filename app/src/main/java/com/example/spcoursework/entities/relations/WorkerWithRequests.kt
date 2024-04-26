package com.example.spcoursework.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.Request

data class WorkerWithRequests(
    @Embedded val request: Request,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val employee: List<Request>
)
