package com.example.beatwell.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.Result
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.remote.response.NewsResponse

class NewsViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getNews(): LiveData<Result<NewsResponse>> = userRepository.getNews()
}