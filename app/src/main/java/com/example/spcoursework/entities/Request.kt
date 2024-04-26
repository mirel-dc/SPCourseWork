package com.example.spcoursework.entities

import android.os.Parcelable
import com.example.spcoursework.R
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(
    tableName = "requests",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["workerId"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ]
)
data class Request(
    @PrimaryKey
    val id: Int,
    val clientId: Int,
    val workerId: UUID?,
    val carNumber: String,
    val problemDescription: String?,
    val status: RequestStatus
)

@Parcelize
enum class RequestStatus() : Parcelable {
    PENDING(),
    WORKING(),
    FINISHED()
}
