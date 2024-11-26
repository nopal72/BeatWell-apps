package com.example.beatwell.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beatwell.R
import com.example.beatwell.data.pref.MessageModel
import com.example.beatwell.utils.getProfileIcon

class ChatBotAdapter: RecyclerView.Adapter<ChatBotAdapter.MessageViewHolder>() {

    private var chatHistory = ArrayList<MessageModel>()
    inner class MessageViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val messageItemView = LayoutInflater.from(parent.context).inflate(viewType,parent,false) as ViewGroup
        return MessageViewHolder(messageItemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val userProfile = holder.itemView.findViewById<ImageView>(R.id.iv_userProfile)
        val userMessageText = holder.itemView.findViewById<TextView>(R.id.tv_userMessageText)

        val message = chatHistory[position]

        userProfile.setImageDrawable(getProfileIcon(userProfile.context, message.isFromUser))
        userMessageText.text = message.text
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatHistory[position].isFromUser){
            R.layout.item_message_local
        } else {
            R.layout.item_message_bot
        }
    }

    override fun getItemCount(): Int = chatHistory.size

    fun addMessage(message: MessageModel){
        chatHistory.add(message)
        notifyDataSetChanged()
    }
}