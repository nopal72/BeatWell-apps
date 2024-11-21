package com.example.beatwell.ui.register

import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = userRepository.register(name,email,password)
}