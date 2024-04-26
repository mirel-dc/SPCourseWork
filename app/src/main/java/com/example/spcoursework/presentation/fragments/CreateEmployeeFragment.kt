package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spcoursework.databinding.FragmentCreateEmployeeBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.EmployeeRoles
import com.example.spcoursework.presentation.viewmodel.CreateEmployeeViewModel

private const val TAG = "CreateEmployeeFragment"

class CreateEmployeeFragment : Fragment() {
    private var _binding: FragmentCreateEmployeeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for CreateEmployeeFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: CreateEmployeeViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateEmployeeViewModel(
                    AutoRepairRepository(
                        AutoRepairDB.getInstance(requireContext())
                    )
                ) as T
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelObservers()
        fieldsListeners()
        focusListeners()
    }


    private fun fieldsListeners() {
        binding.etEmployeeName.addTextChangedListener { name ->
            viewModel.currentEmployee.value?.name = name.toString()
        }

        binding.etEmployeePhoneNumber.addTextChangedListener { phoneNumber ->
            viewModel.currentEmployee.value?.phoneNumber = phoneNumber.toString()
        }

        binding.etEmployeePassword.addTextChangedListener { password ->
            viewModel.currentEmployee.value?.password = password.toString()
        }

        binding.rbWorker.isChecked = true

        binding.rgEmployeeRole.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbWorker.id -> viewModel.currentEmployee
                    .value?.role = EmployeeRoles.WORKER

                binding.rbAdmin.id -> viewModel.currentEmployee
                    .value?.role = EmployeeRoles.ADMIN
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (viewModel.submitBtnAction())
                findNavController().popBackStack()
        }
    }

    private fun initViewModelObservers() {
        viewModel.phoneError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerEmployeePhoneNumber.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerEmployeePassword.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerEmployeeName.helperText =
                errorMessage?.let { resources.getString(it) }
        }
    }

    private fun focusListeners() {
        employeePasswordFocusListener()
        employeeNameFocusListener()
        employeePhoneFocusListener()
    }

    private fun employeeNameFocusListener() {
        binding.etEmployeeName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateName(binding.etEmployeeName.text.toString())
            }
        }
    }

    private fun employeePhoneFocusListener() {
        binding.etEmployeePhoneNumber.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validatePhone(binding.etEmployeePhoneNumber.text.toString())
            }
            Log.d(TAG, binding.etEmployeePhoneNumber.text.toString())
        }
    }

    private fun employeePasswordFocusListener() {
        binding.etEmployeePassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validatePassword(binding.etEmployeePassword.text.toString())
            }
        }
    }
}