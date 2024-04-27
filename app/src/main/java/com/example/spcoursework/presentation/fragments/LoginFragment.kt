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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.spcoursework.databinding.FragmentLoginBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.presentation.viewmodel.RequestListViewModel
import com.example.spcoursework.presentation.viewmodel.factory.RequestListViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "LOGIN_FRAGMENT"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for LoginFragment must not be null")

    private val requestListViewModel: RequestListViewModel by activityViewModels {
        RequestListViewModelFactory(
            AutoRepairRepository(AutoRepairDB.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etLoginPhone.addTextChangedListener { phoneNumber ->
            requestListViewModel.phoneNumber = phoneNumber.toString()
        }

        binding.etLoginPassword.addTextChangedListener { password ->
            requestListViewModel.password = password.toString()
        }

        binding.btnLogIn.setOnClickListener {
            requestListViewModel.login()
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    requestListViewModel.errorSharedFlow.collectLatest {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(it),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        SessionManager.isLogged.observe(viewLifecycleOwner) { result ->
            if (result == true) {
                findNavController().popBackStack()
            }
        }
    }
}