package com.example.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.spcoursework.entities.RequestStatus
import com.example.spcoursework.presentation.fragments.RequestListFragment

class TypeFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = RequestStatus.entries.size - 1

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> RequestListFragment.newInstance(RequestStatus.PENDING)
        else -> RequestListFragment.newInstance(RequestStatus.WORKING)
    }
}
