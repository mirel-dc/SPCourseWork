package com.example.spcoursework.domain.network

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.spcoursework.entities.EmployeeRoles

object SessionManager {
    private const val TOKEN_KEY = "auth_token"
    private const val ROLES = "roles"
    private const val WORKER_ID = "WORKER_ID"
    private const val NAME = "employee_name"

    private lateinit var preferences: SharedPreferences


    var authToken: String?
        get() = preferences.getString(TOKEN_KEY, null)
        set(value) {
            preferences.edit().putString(TOKEN_KEY, value).apply()
        }


    var role: Int?
        get() = preferences.getInt(ROLES, 0)
        set(value) {
            if (value != null) {
                preferences.edit().putInt(ROLES, value).apply()
            }
        }

    var employeeID: String?
        get() = preferences.getString(WORKER_ID, null)
        set(value) {
            preferences.edit().putString(WORKER_ID, value).apply()
        }

    var employeeName: String?
        get() = preferences.getString(NAME, null)
        set(value) {
            preferences.edit().putString(NAME, value).apply()
        }


    var isLogged = MutableLiveData<Boolean>()
        private set

    var roleLiveData = MutableLiveData<EmployeeRoles>()
        private set
    var employeeNameLD = MutableLiveData<String?>()
        private set

    fun setName(name: String?) {
        employeeNameLD.value = name
    }

    fun setRole(role: EmployeeRoles) {
        roleLiveData.value = role
    }


    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        isLogged.value = (authToken != null)
        employeeNameLD.value = employeeName
        roleLiveData.value = role?.let { EmployeeRoles.getByResId(it) }
    }

    fun logout() {
        isLogged.value = false
        preferences.edit().remove(TOKEN_KEY).apply()
        preferences.edit().remove(NAME).apply()
        preferences.edit().remove(ROLES).apply()
        preferences.edit().remove(WORKER_ID).apply()
    }
}