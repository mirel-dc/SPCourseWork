package com.example.spcoursework.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.spcoursework.entities.Request

class RequestDiffItemCallback() : DiffUtil.ItemCallback<Request>() {
    override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem == newItem
    }

}