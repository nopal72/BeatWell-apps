package com.example.beatwell.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.entity.HistoryEntity

class HistoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAlHistory(): LiveData<List<HistoryEntity>> = userRepository.getAllHistory()
}