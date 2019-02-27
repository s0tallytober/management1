package com.venkatesh.schoolmanagement

import android.content.Context
import android.widget.Toast
import com.venkatesh.schoolmanagement.model.ChatMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    val BASE_URL = "https://school-management-system-27ab4.firebaseio.com/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {

            val logging = HttpLoggingInterceptor()
// set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging)  // <-- this is the important line!

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            /*retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()*/
        }
        return retrofit
    }

    fun getChatMessages(context: Context, callBack: RetrofitCallback) {
        val apiService = getClient()?.create(RetrofitApiInteface::class.java)

        val call: Call<List<ChatMessage>> = apiService?.getChatMessages()!!
        call.enqueue(object : Callback<List<ChatMessage>> {
            override fun onResponse(call: Call<List<ChatMessage>>, response: Response<List<ChatMessage>>) {
                if (response.isSuccessful && response.body() != null)
                    callBack.onResponse(response.body())
            }

            override fun onFailure(call: Call<List<ChatMessage>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}