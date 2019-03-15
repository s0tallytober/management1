package com.venkatesh.schoolmanagement

import android.content.Context
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.venkatesh.schoolmanagement.model.ChatMessage
import com.venkatesh.schoolmanagement.model.MaterialUpload
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody
import java.io.*
import java.io.File.separator


object ApiClient {

    private const val BASE_URL = "https://school-management-system-27ab4.firebaseio.com/"
    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
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

    fun getClassMaterials(context: Context, api: String, retrofitCallback: RetrofitCallback) {
        val apiService = getClient()?.create(RetrofitApiInteface::class.java)

        val call: Call<List<MaterialUpload>> = apiService?.getClassMaterials(Constants.materials + "/" + api)!!
        call.enqueue(object : Callback<List<MaterialUpload>> {
            override fun onResponse(call: Call<List<MaterialUpload>>, response: Response<List<MaterialUpload>>) {
                if (response.isSuccessful && response.body() != null)
                    retrofitCallback.onResponse(response.body())
            }

            override fun onFailure(call: Call<List<MaterialUpload>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun downloadDocument(
        context: Context,
        fileUrl: MaterialUpload
    ) {
        val downloadService = getClient()?.create(RetrofitApiInteface::class.java)

        val call = downloadService?.downloadFileWithDynamicUrlSync(fileUrl.materialImageUrl)

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val writtenToDisk = writeResponseBodyToDisk(context, response.body(), fileUrl.materialDescription)
                } else {
                    Toast.makeText(context, "server contact failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    @Synchronized
    private fun writeResponseBodyToDisk(
        context: Context,
        body: ResponseBody?,
        materialDescription: String
    ): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile =
                File("${Environment.getExternalStorageDirectory()}$separator$materialDescription.pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body?.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body?.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d(ApiClient::class.java.simpleName, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()

                Toast.makeText(
                    context,
                    "Material downloaded, Please check in folder in $futureStudioIconFile",
                    Toast.LENGTH_LONG
                ).show()

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }

    }

}