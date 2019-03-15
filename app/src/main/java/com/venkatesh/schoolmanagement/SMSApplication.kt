package com.venkatesh.schoolmanagement

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import com.venkatesh.schoolmanagement.volley.LruBitmapCache

class SMSApplication : Application() {

    private val TAG: String = SMSApplication::class.java.simpleName
    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null
    private var mInstance: SMSApplication? = null

    companion object {
        lateinit var smsApplication: SMSApplication
        fun getInstance() {
            smsApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        smsApplication = this
        FirebaseApp.initializeApp(applicationContext)
    }

    @Synchronized
    fun getInstance(): SMSApplication? {
        return mInstance
    }

    private fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }

    fun getImageLoader(): ImageLoader {
        getRequestQueue()
        if (mImageLoader == null) {
            mImageLoader = ImageLoader(
                this.mRequestQueue,
                LruBitmapCache(LruBitmapCache.getDefaultLruCacheSize())
            )
        }
        return this.mImageLoader!!
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        getRequestQueue()?.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue()?.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

}