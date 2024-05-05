package com.example.spcoursework.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spcoursework.R
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "CreateRequestViewModel"

class CreateRequestViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {
    var currentRequest = MutableLiveData<Request>()
        private set

    var clientPhoneNumber = MutableLiveData<String>()
        private set
    var clientName = MutableLiveData<String>()
        private set

    private val _phoneError = MutableLiveData<Int?>()
    val phoneError: LiveData<Int?> = _phoneError
    private val _carNumberError = MutableLiveData<Int?>()
    val carNumberError: LiveData<Int?> = _carNumberError
    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val clientNameChannel = Channel<String>()
    val clientNameFlow = clientNameChannel.receiveAsFlow()
    private val clientPhoneChannel = Channel<String>()
    val clientPhoneFlow = clientPhoneChannel.receiveAsFlow()

    private val toastChannel = Channel<Int>()
    val toastFlow = toastChannel.receiveAsFlow()


    private var isNewClient = true

    fun initObserver() {
        currentRequest.value = Request(
            id = 0,
            carNumber = "",
            clientPhoneNumber = "",
            workerId = null,
            problemDescription = "",
            status = RequestStatus.PENDING,
            workerCommentary = ""
        )

        initValidationErrors()
        isNewClient = true
    }


    //Returning true for popBackStack if all fine
    fun submitBtnAction(): Boolean {
        return if (isValid()) {
            if (isNewClient) {
                createNewClient()
            }
            createRequest()
            initValidationErrors()
            return true
        } else {
            false
        }
    }

    private lateinit var clientForInsertion: Client
    private fun createNewClient() = viewModelScope.launch {
        clientForInsertion = Client(
            name = clientName.value!!,
            phoneNumber = clientPhoneNumber.value!!,
            carNumber = currentRequest.value!!.carNumber.uppercase()
        )
        autoRepairRepository.insertClient(clientForInsertion)

    }

    private fun createRequest() = viewModelScope.launch {
        currentRequest.value?.clientPhoneNumber = clientPhoneNumber.value!!
        Log.d(TAG, "Creating request " + currentRequest.value.toString())
        currentRequest.value.let {
            if (it != null) {
                autoRepairRepository.insertRequest(it.copy())
            }
        }
    }

    fun findClientWithCarNumber() = viewModelScope.launch {
        val client = autoRepairRepository.getClientWithCarNumber(currentRequest.value?.carNumber)
        if (client == null) {
            toastChannel.send(R.string.can_t_find_client_with_entered_car_s_number)
            isNewClient = true
        } else {
            toastChannel.send(R.string.client_found)
            clientNameChannel.send(client.name)
            clientPhoneChannel.send(client.phoneNumber)
            _carNumberError.value = null
            _phoneError.value = null
            _nameError.value = null
            currentRequest.value?.clientPhoneNumber = client.phoneNumber
            isNewClient = false
        }
    }

    private fun initValidationErrors() {
        _phoneError.value = R.string.cannot_be_empty
        _carNumberError.value = R.string.cannot_be_empty
        _nameError.value = R.string.cannot_be_empty
    }

    private fun isValid(): Boolean {
        val validEntries = (validatePhone(clientPhoneNumber.value.toString())
                && validateCarNumber(currentRequest.value?.carNumber.toString())
                && validateName(clientName.value.toString()))
        return validEntries
    }


    fun validateName(enteredName: String): Boolean {
        return if (enteredName == "") {
            _nameError.value = R.string.cannot_be_empty
            false
        } else {
            _nameError.value = null
            true
        }
    }

    fun validatePhone(enteredPhone: String): Boolean {
        return if (enteredPhone == "" || enteredPhone == "null") {
            _phoneError.value = R.string.cannot_be_empty
            false
        } else if (enteredPhone.length > 11) {
            _phoneError.value = R.string.too_long
            false

        } else if (enteredPhone.length < 11) {
            _phoneError.value = R.string.too_short
            false
        } else {
            _phoneError.value = null
            true
        }
    }

    fun validateCarNumber(enteredCarNumber: String): Boolean {
        val regex = Regex(
            "^[авекмнорстухA-Z]{1}\\d{3}[авекмнорстухA-Z]{2}\\d{2,3}$",
            RegexOption.IGNORE_CASE
        )

        return if (enteredCarNumber == "") {
            _carNumberError.value = R.string.cannot_be_empty
            false
        } else if (enteredCarNumber.length > 8) {
            _carNumberError.value = R.string.too_long
            false
        } else if (!regex.matches(enteredCarNumber)) {
            _carNumberError.value = R.string.wrong_format_a123aa12
            false
        } else {
            _carNumberError.value = null
            true
        }
    }
}