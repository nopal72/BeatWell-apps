package com.example.beatwell.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.Result

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getHistory(): LiveData<List<HistoryEntity>> = userRepository.getAllHistory()

    fun addHistory(request: Int): LiveData<Result<HistoryResponse>> = userRepository.addHistory(request.toDouble())
}