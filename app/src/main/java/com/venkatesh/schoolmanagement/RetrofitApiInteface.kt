package com.venkatesh.schoolmanagement

import com.venkatesh.schoolmanagement.model.ChatMessage
import retrofit2.Call
import retrofit2.http.GET


interface RetrofitApiInteface {
    @GET("chat_history.json")
    fun getChatMessages(): Call<List<ChatMessage>>
}