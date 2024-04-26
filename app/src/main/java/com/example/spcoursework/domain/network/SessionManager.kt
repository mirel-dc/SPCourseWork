package com.example.spcoursework.domain.network

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.spcoursework.entities.EmployeeRoles

object SessionManager {
    private const val TOKEN_KEY = "auth_token"
    private const val ROLES = "roles"
    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        isLogged.value = (authToken != null)
    }

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

    var isLogged = MutableLiveData<Boolean>()
        private set

//    fun isLoggedIn(): Boolean {
//        return authToken != null
//    }

    fun logout() {
        isLogged.value = false
        preferences.edit().remove(TOKEN_KEY).apply()
        preferences.edit().remove(ROLES).apply()
    }
}