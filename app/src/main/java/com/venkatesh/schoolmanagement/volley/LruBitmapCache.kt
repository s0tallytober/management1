package com.venkatesh.schoolmanagement.volley

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class LruBitmapCache(cacheSize: Int) : LruCache<String, Bitmap>(cacheSize), ImageLoader.ImageCache {
    override fun getBitmap(url: String?): Bitmap {
        return get(url)
    }

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        put(url, bitmap)
    }

    override fun sizeOf(key: String?, value: Bitmap?): Int {
        value?.let {
            return (value.rowBytes * value.height) / 1024
        }
        return 0
    }

    companion object {
        fun getDefaultLruCacheSize(): Int {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            return maxMemory / 8
        }
    }
}