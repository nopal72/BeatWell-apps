package com.example.beatwell.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beatwell.data.remote.response.HistoryItem
import com.example.beatwell.databinding.HistoryCardBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter: ListAdapter<HistoryItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: HistoryCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryItem){
            binding.tvDate.text = formatdDate(data.createdAt)
            binding.tvStatus.text = data.result.toString()
        }

        private fun formatdDate(createdAt: String): CharSequence? {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val date = LocalDateTime.parse(createdAt, inputFormatter)
            val formattedDate = date.format(outputFormatter)

            return formattedDate
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}