package com.example.spcoursework.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spcoursework.R
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "RequestListViewModel"

class RequestListViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {

    var requestsLiveData: LiveData<List<Request>> = autoRepairRepository.getAllRequests()
        private set

    private var currentList: List<Request> = listOf()

    var phoneNumber: String? = null
    var password: String? = null


    private val errorChannel = Channel<Int>()
    val errorSharedFlow = errorChannel.receiveAsFlow()


    fun getRequestsByStatus(requestStatus: RequestStatus): List<Request> {
        val list = currentList.filter { it.status == requestStatus }
        return when (requestStatus) {
            RequestStatus.WORKING -> {
                list.filter { it.workerId == UUID.fromString(SessionManager.employeeID) }
            }
            else -> {
                list
            }
        }
    }

    fun updateLiveData() {
        requestsLiveData = autoRepairRepository.getAllRequests()
    }


    fun setCurrentList(newList: List<Request>) {
        currentList = newList
    }

    fun login() = viewModelScope.launch {
        val employeeAsync = async {
            phoneNumber?.let { autoRepairRepository.getEmployeeWithPhone(it) }
        }
        val employee = employeeAsync.await()
        val correctPassword = employee?.password

        if (correctPassword == password && password != null) {
            Log.d(TAG, "$employee")
            //Live data
            SessionManager.setName(employee?.name)
            SessionManager.setRole(employee?.role!!)
            SessionManager.isLogged.value = true

            //Pref files
            SessionManager.employeeName = employee.name
            SessionManager.role = employee.role.resId
            SessionManager.authToken = "TOKEN"
            SessionManager.employeeID = employee.id.toString()
        } else {
            errorChannel.send(R.string.wrong_password)
        }
    }
}