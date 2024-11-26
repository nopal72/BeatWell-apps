package com.example.beatwell.ui.foodDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.remote.response.FoodDetailResponse
import com.example.beatwell.data.Result

class FoodDetailViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getFoodDetail(id: Int): LiveData<Result<FoodDetailResponse>> = repository.getFoodDetail(id)
}