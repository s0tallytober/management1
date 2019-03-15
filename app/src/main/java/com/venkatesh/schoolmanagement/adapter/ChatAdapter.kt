package com.venkatesh.schoolmanagement.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.ChatMessage

class ChatAdapter(private var chats: List<ChatMessage>) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.adapter_item_chat, p0, false))
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(p0: ChatViewHolder, p1: Int) {
        val chatMessage = chats[p1]

        p0.run {
            message_user.text = chatMessage.messageUser
            message_text.text = chatMessage.messageText
            message_time.text = chatMessage.messageTime
        }
    }

    fun updateData(list: ArrayList<ChatMessage>) {
        this.chats = list
        notifyDataSetChanged()
    }
}

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val message_user: TextView = view.findViewById<TextView>(R.id.message_user)
    val message_text: TextView = view.findViewById<TextView>(R.id.message_text)
    val message_time: TextView = view.findViewById<TextView>(R.id.message_time)
}
