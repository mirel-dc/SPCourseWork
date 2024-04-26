package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spcoursework.databinding.FragmentRequestsListBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.repository.AutoRepairRepository
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus
import com.example.spcoursework.presentation.adapters.OnRecyclerItemClicked
import com.example.spcoursework.presentation.adapters.RecyclerViewAdapter
import com.example.spcoursework.presentation.utils.SpacingItemDecorator
import com.example.spcoursework.presentation.utils.parcelable
import com.example.spcoursework.presentation.viewmodel.RequestListViewModel
import com.example.spcoursework.presentation.viewmodel.factory.RequestListViewModelFactory

private const val TAG = "RequestListFragment"

class RequestListFragment : Fragment() {

    private var _binding: FragmentRequestsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRequestsList must not be null")

    private var _adapter: RecyclerViewAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private val viewModel: RequestListViewModel by activityViewModels {
        RequestListViewModelFactory(
            AutoRepairRepository(AutoRepairDB.getInstance(requireContext()))
        )
    }

    private lateinit var requestStatus: RequestStatus


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        arguments?.takeIf { it.containsKey(PARAM_TYPE) }?.apply {
            requestStatus = parcelable(PARAM_TYPE)!!
        }
        Log.d(TAG,requestStatus.toString())

        AutoRepairDB.getInstance(requireContext()).getDao().getAllRequests()
            .observe(viewLifecycleOwner) {
                viewModel.updateLiveData()
            }

        viewModel.requestsLiveData.observe(viewLifecycleOwner) { newList ->
            viewModel.setCurrentList(newList)
            adapter.submitList(viewModel.getRequestsByStatus(requestStatus))
        }
    }

    private fun initRecyclerView() {
        _adapter = context?.let {
            RecyclerViewAdapter(it, clickListener)
        }

        binding.rvHabit.adapter = adapter
        binding.rvHabit.addItemDecoration(SpacingItemDecorator(16))
        binding.rvHabit.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
    }

    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(request: Request) {
            doOnRVItemClicked(request)
        }
    }


    private fun doOnRVItemClicked(request: Request) {
        Toast.makeText(requireContext(), "$request", Toast.LENGTH_LONG).show()
//        val navAction =
//            MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(habit.id.toString())
//        findNavController().navigate(navAction)
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