package com.venkatesh.schoolmanagement.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.admin.AdminDashboardActivity

class SplashActivity : Activity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            checkPreviousLoginSession()
        }, 2000)
    }

    private fun checkPreviousLoginSession() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(Intent(this@SplashActivity, SelectLoginTypeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashActivity, AdminDashboardActivity::class.java))
            finish()
        }
    }

}
