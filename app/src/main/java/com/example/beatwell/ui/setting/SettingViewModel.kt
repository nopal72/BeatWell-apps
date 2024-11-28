package com.example.beatwell.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.beatwell.data.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val userRepository: UserRepository): ViewModel() {
    fun logOut() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getDailyReminder(): LiveData<Boolean> {
        return userRepository.getDailyReminder().asLiveData()
    }

    fun setDailyReminder(dailyReminder: Boolean) {
        viewModelScope.launch {
            userRepository.setDailyReminder(dailyReminder)
        }
    }
}