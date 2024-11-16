package com.example.beatwell.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beatwell.data.remote.response.FoodDataItem
import com.example.beatwell.databinding.ItemCardBinding

class FoodAdapter: ListAdapter<FoodDataItem, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FoodDataItem){
            binding.cardTitle.text = data.name
            binding.cardDescription.text = data.recipe
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodDataItem>() {
            override fun areContentsTheSame(oldItem: FoodDataItem, newItem: FoodDataItem): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: FoodDataItem, newItem: FoodDataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}