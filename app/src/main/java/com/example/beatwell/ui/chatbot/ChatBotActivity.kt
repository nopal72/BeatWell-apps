package com.example.beatwell.ui.chatbot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.data.pref.MessageModel
import com.example.beatwell.databinding.ActivityChatBotBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var chatBotAdapter: ChatBotAdapter
    private val viewModel: ChatBotViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initChatHistoryRecyclerView()
        binding.ibSend.setOnClickListener {
            val userMessage = binding.tietQuestion.text.toString()
            if (userMessage.isNotEmpty()){
                sendMessage(userMessage)
            }
        }
    }

    private fun sendMessage(userMessage: String) {
        val userMessageModel = MessageModel(userMessage,true)
        chatBotAdapter.addMessage(userMessageModel)
        binding.rvChatHistory.scrollToPosition(chatBotAdapter.itemCount - 1)
        binding.tietQuestion.text?.clear()

        viewModel.chatbot(userMessage).observe(this){result->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val botMessage = result.data
                    chatBotAdapter.addMessage(botMessage)
                    binding.rvChatHistory.scrollToPosition(chatBotAdapter.itemCount - 1)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initChatHistoryRecyclerView() {
        val historyLayoutManager = LinearLayoutManager(this)
        binding.rvChatHistory.layoutManager = historyLayoutManager

        chatBotAdapter = ChatBotAdapter()
        binding.rvChatHistory.adapter = chatBotAdapter
    }
}