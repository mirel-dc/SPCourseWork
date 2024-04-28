package com.example.spcoursework.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.spcoursework.R
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(
    tableName = "requests",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["phoneNumber"],
            childColumns = ["clientPhoneNumber"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ]
)
data class Request(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var clientPhoneNumber: String,
    var workerId: UUID?,
    var carNumber: String,
    var problemDescription: String?,
    var status: RequestStatus,
    var workerCommentary: String?
)

@Parcelize
enum class RequestStatus(val resId: Int) : Parcelable {
    PENDING(R.string.pending),
    WORKING(R.string.working),
    FINISHED(R.string.finished)
}
