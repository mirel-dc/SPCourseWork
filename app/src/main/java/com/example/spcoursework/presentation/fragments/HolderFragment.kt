package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.adapters.TypeFragmentAdapter
import com.example.spcoursework.R
import com.example.spcoursework.databinding.FragmentHolderBinding
import com.example.spcoursework.domain.network.SessionManager
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "HolderFragment"

class HolderFragment : Fragment() {
    private var _binding: FragmentHolderBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for HolderFragment must not be null")

    private lateinit var adapter: TypeFragmentAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionManager.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SessionManager.isLogged.observe(viewLifecycleOwner) {
            if (!it) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        adapter = TypeFragmentAdapter(this)
        viewPager = binding.requestTypeViewPager2
        viewPager.adapter = adapter
        TabLayoutMediator(
            binding.requestTypeTabLayout,
            binding.requestTypeViewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.arrived)
                1 -> tab.text = getString(R.string.my_working)
            }
        }.attach()
    }
}