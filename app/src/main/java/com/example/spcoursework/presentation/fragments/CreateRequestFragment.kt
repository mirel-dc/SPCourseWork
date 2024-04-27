package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.spcoursework.databinding.FragmentCreateRequestBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.presentation.viewmodel.CreateRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateRequestFragment : Fragment() {
    private var _binding: FragmentCreateRequestBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for CreateRequestFragment must not be null")

    private val viewModel: CreateRequestViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateRequestViewModel(
                    AutoRepairRepository(
                        AutoRepairDB.getInstance(requireContext())
                    )
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initObserver()

        fieldsListeners()
        initViewModelObservers()
        focusListeners()
    }


    private fun fieldsListeners() {
        binding.etClientName.addTextChangedListener { name ->
            viewModel.clientName.value = name.toString()
        }

        binding.etCarNumber.addTextChangedListener { carNumber ->
            viewModel.currentRequest.value?.carNumber = carNumber.toString()
        }

        binding.etClientPhoneNumber.addTextChangedListener { phoneNumber ->
            viewModel.clientPhoneNumber.value = phoneNumber.toString()
        }

        binding.etDescription.addTextChangedListener { description ->
            viewModel.currentRequest.value?.problemDescription = description.toString()
        }

        binding.btnSubmit.setOnClickListener {
            if (viewModel.submitBtnAction())
                findNavController().popBackStack()
        }

        binding.btnFindWithCarNumber.setOnClickListener {
            viewModel.findClientWithCarNumber()
        }
    }

    private fun initViewModelObservers() {
        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerClientName.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.phoneError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerClientPhoneNumber.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.carNumberError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerCarNumber.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.clientNameFlow.collectLatest {
                        binding.etClientName.setText(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.clientPhoneFlow.collectLatest {
                        binding.etClientPhoneNumber.setText(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.toastFlow.collectLatest {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(it),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun focusListeners() {
        clientNameFocusListener()
        carNumberFocusListener()
        phoneNumberFocusListener()
    }

    private fun clientNameFocusListener() {
        binding.etClientName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateName(binding.etClientName.text.toString())
            }
        }
    }

    private fun carNumberFocusListener() {
        binding.etCarNumber.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateCarNumber(binding.etCarNumber.text.toString())
            }
        }
    }

    private fun phoneNumberFocusListener() {
        binding.etClientPhoneNumber.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validatePhone(binding.etClientPhoneNumber.text.toString())
            }
        }
    }
}