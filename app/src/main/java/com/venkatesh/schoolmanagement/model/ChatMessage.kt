package com.venkatesh.schoolmanagement.model

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("messageText")
    val messageText: String = "",
    @SerializedName("messageUser")
    val messageUser: String = "",
    @SerializedName("messageTime")
    val messageTime: String = "",
    @SerializedName("usesrid")
    val usesrid: String = ""
)

