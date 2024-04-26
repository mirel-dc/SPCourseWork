package com.example.spcoursework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spcoursework.R
import com.example.spcoursework.databinding.FragmentCreateRequestBinding
import com.example.spcoursework.presentation.viewmodel.RequestListViewModel

class CreateRequestFragment : Fragment() {
    private var _binding: FragmentCreateRequestBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for CreateRequestFragment must not be null")

    private val viewModel: RequestListViewModel by activityViewModels()

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


    }

}