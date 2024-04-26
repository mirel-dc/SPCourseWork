package com.example.spcoursework.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spcoursework.domain.repository.AutoRepairRepository

class RequestListViewModelFactory(private val autoRepairRepository: AutoRepairRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AutoRepairRepository::class.java)
            .newInstance(autoRepairRepository)
    }
}