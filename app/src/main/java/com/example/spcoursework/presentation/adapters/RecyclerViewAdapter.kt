package com.example.spcoursework.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spcoursework.R
import com.example.spcoursework.databinding.ListItemRequestBinding
import com.example.spcoursework.entities.Request

class RecyclerViewAdapter(
    context: Context,
    private val clickListener: OnRecyclerItemClicked,
) : ListAdapter<Request, RecyclerViewAdapter.ViewHolder>(RequestDiffItemCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.list_item_request, parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItem(position: Int): Request = currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onRVItemClicked(currentList[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemRequestBinding.bind(view)

        fun bind(request: Request) = with(binding) {
            tvRequestId.text = context.getString(R.string.requestNumber, request.id)
            tvCarNumber.text = request.carNumber
            tvRequestDescription.text = request.problemDescription
            tvRequestStatus.text = context.getString(request.status.resId)
           // tvWorkerName.text = request.workerId.toString()
        }
    }
}

interface OnRecyclerItemClicked {
    fun onRVItemClicked(request: Request)
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context