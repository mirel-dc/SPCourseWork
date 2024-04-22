package com.example.spcoursework.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spcoursework.R
import com.example.spcoursework.data.models.Request

class RequestAdapter(
    context: Context,
    private val clickListener: OnRecyclerItemClicked,
) : ListAdapter<Request, RequestAdapter.ViewHolder>(HabitDiffItemCallback()) {

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
        private val binding = ItemHabitatDataBinding.bind(view)

        fun bind(request: Request) = with(binding) {
            tvHabitName.text = habit.name
            tvHabitDescription.text = habit.description
            tvHabitType.text = context.getString(habit.type.resId)
            tvHabitFrequency.text = context.getString(R.string.week, habit.frequency.toString())
            viewColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(habit.color, 1f, 1f)))
            tvHabitPriority.text = habit.priority.toString()
        }
    }
}

interface OnRecyclerItemClicked {
    fun onRVItemClicked(request: Request)
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context