package com.example.beatwell.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.entity.HistoryEntity

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getHistory(): LiveData<List<HistoryEntity>> = userRepository.getAllHistory()
}