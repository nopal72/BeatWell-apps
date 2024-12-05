package com.example.beatwell.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.pref.UserModel

class SplashViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()
}