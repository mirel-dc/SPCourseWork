package com.example.spcoursework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.domain.repository.AutoRepairRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private const val TAG = "RequestListViewModel"

class RequestListViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {

    var phoneNumber: String? = null
    var password: String? = null

    val errorSharedFlow = MutableSharedFlow<String>()

    fun login() = viewModelScope.launch {
        val employeeAsync = async {
            phoneNumber?.let { autoRepairRepository.getEmployeeWithPhone(it) }
        }
        val employee = employeeAsync.await()
        val correctPassword = employee?.password

        if (correctPassword == password && password != null) {
            SessionManager.isLogged.value = true
            SessionManager.authToken = "TOKEN"
            SessionManager.role = employee!!.role.resId
        } else {
            errorSharedFlow.emit("Wrong Password")
        }
    }
}