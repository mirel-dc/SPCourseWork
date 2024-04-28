package com.example.spcoursework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus
import kotlinx.coroutines.launch
import java.util.UUID

class TakeRequestViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {

    private val _currentRequest = MutableLiveData<Request>()
    val currentRequest: LiveData<Request> = _currentRequest

    var workerCommentary: String? = null

    fun setCurrentRequest(requestId: Int) = viewModelScope.launch {
        _currentRequest.value = autoRepairRepository.getRequestWithId(requestId)
    }

    fun takeRequest(): Boolean {
        setStatusToWorking()
        return true
    }

    private fun setStatusToWorking() = viewModelScope.launch {
        currentRequest.value!!.status = RequestStatus.WORKING
        currentRequest.value!!.workerId = UUID.fromString(SessionManager.employeeID)
        autoRepairRepository.updateRequest(currentRequest.value!!)
    }

    fun returnRequest(): Boolean {
        viewModelScope.launch {
            currentRequest.value!!.workerCommentary = workerCommentary
            currentRequest.value!!.status = RequestStatus.PENDING
            autoRepairRepository.updateRequest(currentRequest.value!!)
        }
        return true
    }

    fun finishRequest(): Boolean {
        viewModelScope.launch {
            currentRequest.value!!.workerCommentary = workerCommentary
            currentRequest.value!!.status = RequestStatus.FINISHED
            autoRepairRepository.updateRequest(currentRequest.value!!)
        }
        return true
    }
}