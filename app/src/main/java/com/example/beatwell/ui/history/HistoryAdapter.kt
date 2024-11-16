package com.example.beatwell.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beatwell.data.remote.response.HistoryDataItem
import com.example.beatwell.databinding.HistoryCardBinding
import com.example.beatwell.databinding.ItemCardBinding

class HistoryAdapter: ListAdapter<HistoryDataItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: HistoryCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryDataItem){
            binding.tvDate.text = data.lastChecked
            binding.tvStatus.text = data.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryDataItem>() {
            override fun areContentsTheSame(oldItem: HistoryDataItem, newItem: HistoryDataItem): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: HistoryDataItem, newItem: HistoryDataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}