package com.example.beatwell.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.Result
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.data.remote.response.ActivityResponse
import com.example.beatwell.data.remote.response.TriviaResponse

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getHistory(): LiveData<Result<HistoryEntity>> = userRepository.getHistory()

    fun getActivity(): LiveData<Result<ActivityResponse>> = userRepository.getActivity()

    fun getTrivia(): LiveData<Result<TriviaResponse>> = userRepository.getTrivia()
}