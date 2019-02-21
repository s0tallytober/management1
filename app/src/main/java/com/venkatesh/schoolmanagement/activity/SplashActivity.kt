package com.venkatesh.schoolmanagement.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.venkatesh.schoolmanagement.R

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, SelectLoginTypeActivity::class.java))
            finish()
        }, 2000)
    }

}
