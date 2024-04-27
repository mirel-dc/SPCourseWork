package com.example.spcoursework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

private const val TAG = "RequestListViewModel"

class RequestListViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {

    var requestsLiveData: LiveData<List<Request>> = autoRepairRepository.getAllRequests()
        private set

    private var currentList: List<Request> = listOf()

    var phoneNumber: String? = null
    var password: String? = null

    private val _searchNameLiveData = MutableLiveData<String>()
    val searchNameLiveData: LiveData<String> = _searchNameLiveData

    private val errorChannel = Channel<Int>()
    val errorSharedFlow = errorChannel.receiveAsFlow()

    init {
        _searchNameLiveData.value = ""
    }

    fun getRequestsByStatus(requestStatus: RequestStatus): List<Request> {
        val list = currentList.filter { it.status == requestStatus }
        val searchString = searchNameLiveData.value.toString().trim()

        return list.filter {
            it.id.toString().lowercase().contains(searchString.lowercase())
        }
    }

    fun updateLiveData() {
        requestsLiveData = autoRepairRepository.getAllRequests()
    }

    fun setSearchingName(name: String) {
        _searchNameLiveData.value = name
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
            SessionManager.isLogged.value = true
            SessionManager.authToken = "TOKEN"
            SessionManager.role = employee!!.role.resId
        } else {
            errorChannel.send(R.string.wrong_password)
        }
    }
}