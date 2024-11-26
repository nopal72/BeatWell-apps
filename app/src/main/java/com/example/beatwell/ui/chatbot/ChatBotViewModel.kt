package com.example.beatwell.ui.chatbot

import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository

class ChatBotViewModel(private val repository: UserRepository) : ViewModel() {
    fun chatbot(message: String) = repository.chatbot(message)
}