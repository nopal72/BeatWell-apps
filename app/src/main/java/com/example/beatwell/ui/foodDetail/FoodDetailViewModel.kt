package com.example.beatwell.ui.foodDetail

import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository

class FoodDetailViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getFoodDetail(id: Int) = repository.getFoodDetail(id)
}