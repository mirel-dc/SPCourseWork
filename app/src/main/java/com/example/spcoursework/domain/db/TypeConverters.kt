package com.example.spcoursework.domain.db

import androidx.room.TypeConverter
import com.example.spcoursework.entities.EmployeeRoles
import java.util.UUID

open class EmployeeRoleConverter {
    @TypeConverter
    fun fromHabitType(employeeRoles: EmployeeRoles): Int {
        return employeeRoles.resId
    }

    @TypeConverter
    fun toHabitType(resId: Int): EmployeeRoles {
        return EmployeeRoles.getByResId(resId)
    }
}

open class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID?): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}
