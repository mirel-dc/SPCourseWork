package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.spcoursework.R
import com.example.spcoursework.databinding.FragmentRequestsListBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.RequestStatus
import com.example.spcoursework.presentation.adapters.RequestAdapter
import com.example.spcoursework.presentation.viewmodel.RequestListViewModel
import com.example.spcoursework.presentation.viewmodel.factory.RequestListViewModelFactory

private const val TAG = "RequestListFragment"

class RequestListFragment : Fragment() {

    private var _binding: FragmentRequestsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRequestsList must not be null")

    private var _adapter: RequestAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private val viewModel: RequestListViewModel by activityViewModels {
        RequestListViewModelFactory(
            AutoRepairRepository(AutoRepairDB.getInstance(requireContext()))
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val PARAM_TYPE = "param_type"

        fun newInstance(requestStatus: RequestStatus): RequestListFragment {
            val fragment = RequestListFragment()
            val args = Bundle()
            args.putParcelable(PARAM_TYPE, requestStatus)
            fragment.arguments = args
            return fragment
        }
    }
}