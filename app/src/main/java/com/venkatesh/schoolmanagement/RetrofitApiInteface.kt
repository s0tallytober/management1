package com.venkatesh.schoolmanagement

import com.venkatesh.schoolmanagement.model.ChatMessage
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.model.UserProfile
import retrofit2.Call
import retrofit2.http.GET


interface RetrofitApiInteface {
    @GET("chat_history.json")
    fun getChatMessages(): Call<List<ChatMessage>>

    @GET("events.json")
    fun getEvents(): Call<List<SMSEvent>>?

    @GET("students.json")
    fun getStudents(): Call<List<UserProfile>>?

    @GET("Teacher.json")
    fun getTeachers(): Call<List<UserProfile>>?
}