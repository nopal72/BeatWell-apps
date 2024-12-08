package com.example.beatwell.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.Result

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = userRepository.login(email, password)
}