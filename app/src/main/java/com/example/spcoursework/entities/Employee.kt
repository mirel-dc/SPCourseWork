package com.example.spcoursework.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spcoursework.R
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var role: EmployeeRoles,
    var phoneNumber: String,
    var password: String
)

@Parcelize
enum class EmployeeRoles(val resId: Int) : Parcelable {
    ADMIN(R.string.admin),
    WORKER(R.string.worker);

    companion object {
        fun getByResId(resId: Int): EmployeeRoles {
            return when (resId) {
                ADMIN.resId -> ADMIN
                else -> WORKER
            }
        }
    }
}
