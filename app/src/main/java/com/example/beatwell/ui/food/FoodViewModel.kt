package com.example.beatwell.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.Result

class FoodViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFoods(): LiveData<Result<FoodsResponse>>  = userRepository.getFoods()
}