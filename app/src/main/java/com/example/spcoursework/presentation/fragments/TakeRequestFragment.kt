package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.spcoursework.R
import com.example.spcoursework.databinding.FragmentTakeRequestBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.RequestStatus
import com.example.spcoursework.presentation.viewmodel.TakeRequestViewModel

class TakeRequestFragment : Fragment() {
    private var _binding: FragmentTakeRequestBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for TakeRequestFragment must not be null")

    private val viewModel: TakeRequestViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TakeRequestViewModel(
                    AutoRepairRepository(
                        AutoRepairDB.getInstance(requireContext())
                    )
                ) as T
            }
        }
    }
    private val args: TakeRequestFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setCurrentRequest(args.requestId)

        fieldsListeners()
        initViewModelObservers()
    }

    private fun initViewModelObservers() = with(binding) {
        viewModel.currentRequest.observe(viewLifecycleOwner) { request ->
            setStatusVisibility(request.status)

            //Static Fields
            tvRequestId.text = resources.getString(R.string.requestNumber, request.id)
            tvCarNumber.text = resources.getString(R.string.carNumber, request.carNumber)
            tvClientPhone.text =
                resources.getString(R.string.clientsPhone, request.clientPhoneNumber)
            etProblemDescription.setText(request.problemDescription)
            etWorkerCommentary.setText(request.workerCommentary)
            tvRequestStatus.text =
                resources.getString(R.string.currentStatus, request.status.toString())
        }
    }

    private fun fieldsListeners() = with(binding) {
        btnTakeRequest.setOnClickListener {
            if (viewModel.takeRequest()) {
                findNavController().popBackStack()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.request_successfully_taken),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnReturnRequest.setOnClickListener {
            if (viewModel.returnRequest())
                findNavController().popBackStack()
        }

        btnFinishRequest.setOnClickListener {
            if (viewModel.finishRequest())
                findNavController().popBackStack()
        }

        etWorkerCommentary.addTextChangedListener { commentary ->
            viewModel.workerCommentary = commentary.toString()
        }
    }

    private fun setStatusVisibility(requestStatus: RequestStatus) = with(binding) {
        val flag = (requestStatus == RequestStatus.PENDING)

        btnTakeRequest.isVisible = flag
        alreadyTakenRequestGroup.isVisible = !flag
        containerWorkerCommentary.isVisible = !flag
    }
}