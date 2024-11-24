package com.example.beatwell.ui.predict

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.data.remote.response.PredictResponse
import com.example.beatwell.data.Result
import com.example.beatwell.data.entity.HistoryEntity

class PredictViewModel(private val repository: UserRepository) : ViewModel() {
    fun predict(request: PredictRequest): LiveData<Result<HistoryEntity>> = repository.predict(request)
}