package com.example.beatwell.ui.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.Result
import com.example.beatwell.data.pref.UserModel

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = userRepository.login(email, password)
    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()
}