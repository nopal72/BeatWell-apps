package com.example.beatwell.ui.food

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beatwell.data.remote.response.FoodItem
import com.example.beatwell.databinding.FoodItemCardBinding
import com.example.beatwell.ui.foodDetail.FoodDetailActivity

class FoodAdapter: ListAdapter<FoodItem, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: FoodItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FoodItem){
            binding.cardTitle.text = data.name
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.ivCard)

            itemView.setOnClickListener {
                val foodDetail = Intent(itemView.context, FoodDetailActivity::class.java)
                foodDetail.putExtra("id", data.id)
                itemView.context.startActivity(foodDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FoodItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodItem>() {
            override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}