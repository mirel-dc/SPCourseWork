package com.example.spcoursework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spcoursework.R
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.EmployeeRoles
import kotlinx.coroutines.launch

class CreateEmployeeViewModel(
    private val autoRepairRepository: AutoRepairRepository
) : ViewModel() {
    var currentEmployee = MutableLiveData<Employee>()
        private set

    private val _phoneError = MutableLiveData<Int?>()
    val phoneError: LiveData<Int?> = _phoneError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?> = _passwordError

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    init {
        currentEmployee.value = Employee(
            name = "",
            password = "",
            phoneNumber = "",
            role = EmployeeRoles.WORKER
        )
        initValidationErrors()
    }


    //Returning true for popBackStack if all fine
    fun submitBtnAction(): Boolean {
        return if (isValid()) {
            createEmployee()
            return true
        } else {
            false
        }
    }

    private fun createEmployee() = viewModelScope.launch {
        currentEmployee.value.let {
            if (it != null) {
                autoRepairRepository.insertEmployee(it.copy())
            }
        }
    }

    private fun initValidationErrors() {
        _phoneError.value = R.string.cannot_be_empty
        _passwordError.value = R.string.cannot_be_empty
        _nameError.value = R.string.cannot_be_empty
    }

    private fun isValid(): Boolean {
        return (validatePhone(currentEmployee.value?.phoneNumber.toString())
                && validatePassword(currentEmployee.value?.password.toString())
                && validateName(currentEmployee.value?.name.toString()))
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
        return if (enteredPhone == "") {
            _phoneError.value = R.string.cannot_be_empty
            false
        } else if (enteredPhone.length > 11) {
            _phoneError.value = R.string.too_long
            false
        } else {
            _phoneError.value = null
            true
        }
    }

     fun validatePassword(enteredPassword: String): Boolean {
        if (enteredPassword.isEmpty()) {
            _passwordError.value = R.string.cannot_be_empty
            return false
        }

        // Проверка длины пароля
        if (enteredPassword.length < 5) {
            _passwordError.value = R.string.password_is_too_short
            return false
        }

        // Проверка наличия цифры
        val containsDigit = enteredPassword.any { it.isDigit() }
        if (!containsDigit) {
            _passwordError.value = R.string.at_least_1_digit
            return false
        }

        // Проверка наличия хотя бы одной маленькой и одной большой буквы
        val containsUpperCase = enteredPassword.any { it.isUpperCase() }
        val containsLowerCase = enteredPassword.any { it.isLowerCase() }
        if (!containsUpperCase || !containsLowerCase) {
            _passwordError.value = R.string.at_least_1_capital_and_lowercase
            return false
        }
        _passwordError.value = null
        return true
    }
}