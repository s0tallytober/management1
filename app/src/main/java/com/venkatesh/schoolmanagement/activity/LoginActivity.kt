package com.venkatesh.schoolmanagement.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*
import com.venkatesh.schoolmanagement.R
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.view.*

class LoginActivity : AppCompatActivity() {
    var loginType: String = ""
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance()

        title = getString(R.string.btn_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(Constants.loginType)) {
            when (intent.getStringExtra(Constants.loginType)) {
                Constants.admin -> {
                    title = getString(R.string.admin_login)
                }

                Constants.studnet -> {
                    title = getString(R.string.student_login)
                }

                Constants.teacher -> {
                    title = getString(R.string.teacher_login)
                }
            }
        }

        btnLogin.setOnClickListener { loginClick() }
    }

    private fun loginClick() {
        if (validateLogin()) {
/*
            mAuth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        callDashboardScreen()
                    } else {
                        Commons.showAlertDialog(
                            context = this@LoginActivity,
                            message = getString(R.string.email_not_exist)
                        )
                    }
                })
*/
        }
    }

    private fun callDashboardScreen() {
        when (intent.getStringExtra(Constants.loginType)) {
            Constants.admin -> {
                val intent = Intent(this@LoginActivity, AdminDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                finishAffinity()
                startActivity(intent)
            }

            Constants.studnet -> {
            }

            Constants.teacher -> {
            }
        }
    }

    private fun validateLogin(): Boolean {
        return when {
            etEmail.text.isEmpty() or !(etEmail.text.isValidEmail()) -> {
                Commons.showAlertDialog(context = this@LoginActivity, message = getString(R.string.valid_email))
                false
            }
            etPassword.text.isEmpty() or (etPassword.text.length < 6) -> {
                Commons.showAlertDialog(context = this@LoginActivity, message = getString(R.string.valid_password))
                false
            }
            else -> true
        }
    }
}

private fun CharSequence.isValidEmail(): Boolean {
    return  android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
}
