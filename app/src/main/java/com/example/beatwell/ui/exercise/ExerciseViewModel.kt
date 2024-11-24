package com.example.beatwell.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.beatwell.data.Result
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.entity.HistoryEntity
import java.util.concurrent.Executors

class ExerciseViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getLastHistory(callback: (HistoryEntity?) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            val lastHistory = userRepository.getLastHistory()
            callback(lastHistory)
        }
    }
}