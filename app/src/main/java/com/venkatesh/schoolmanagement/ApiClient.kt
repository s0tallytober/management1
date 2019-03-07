package com.venkatesh.schoolmanagement

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.venkatesh.schoolmanagement.model.ChatMessage
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.model.UserProfile
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

    fun getEvents(eventsActivity: AppCompatActivity, retrofitCallback: RetrofitCallback) {
        val apiService = getClient()?.create(RetrofitApiInteface::class.java)

        val call: Call<List<SMSEvent>> = apiService?.getEvents()!!
        call.enqueue(object : Callback<List<SMSEvent>> {
            override fun onResponse(call: Call<List<SMSEvent>>, response: Response<List<SMSEvent>>) {
                if (response.isSuccessful && response.body() != null)
                    retrofitCallback.onResponse(response.body())
            }

            override fun onFailure(call: Call<List<SMSEvent>>, t: Throwable) {
                Toast.makeText(eventsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getStudents(eventsActivity: AppCompatActivity, retrofitCallback: RetrofitCallback) {
        val apiService = getClient()?.create(RetrofitApiInteface::class.java)

        val call: Call<List<UserProfile>> = apiService?.getStudents()!!
        call.enqueue(object : Callback<List<UserProfile>> {
            override fun onResponse(call: Call<List<UserProfile>>, response: Response<List<UserProfile>>) {
                if (response.isSuccessful && response.body() != null)
                    retrofitCallback.onResponse(response.body())
            }

            override fun onFailure(call: Call<List<UserProfile>>, t: Throwable) {
                Toast.makeText(eventsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getTeachers(eventsActivity: AppCompatActivity, retrofitCallback: RetrofitCallback) {
        val apiService = getClient()?.create(RetrofitApiInteface::class.java)

        val call: Call<List<UserProfile>> = apiService?.getTeachers()!!
        call.enqueue(object : Callback<List<UserProfile>> {
            override fun onResponse(call: Call<List<UserProfile>>, response: Response<List<UserProfile>>) {
                if (response.isSuccessful && response.body() != null)
                    retrofitCallback.onResponse(response.body())
            }

            override fun onFailure(call: Call<List<UserProfile>>, t: Throwable) {
                Toast.makeText(eventsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}