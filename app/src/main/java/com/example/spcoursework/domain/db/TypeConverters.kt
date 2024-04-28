package com.example.spcoursework.domain.db

import androidx.room.TypeConverter
import com.example.spcoursework.entities.EmployeeRoles

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
