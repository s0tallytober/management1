package com.venkatesh.schoolmanagement.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.admin.AdminDashboardActivity
import com.venkatesh.schoolmanagement.activity.student.StudentDashboardActivity
import com.venkatesh.schoolmanagement.activity.teacher.TeacherDashboardActivity
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var userType: String
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        title = getString(R.string.btn_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (intent.hasExtra(Constants.loginType)) {
            when (intent.getStringExtra(Constants.loginType)) {
                Constants.admin -> {
                    userType = Constants.admin
                    title = getString(R.string.admin_login)
                }

                Constants.studnet -> {
                    userType = Constants.studnet
                    title = getString(R.string.student_login)
                }

                Constants.teacher -> {
                    userType = Constants.teacher
                    title = getString(R.string.teacher_login)
                }
            }
        }

        btnLogin.setOnClickListener { loginClick() }
    }

    private fun loginClick() {
        if (validateLogin()) {
            progressBar.visibility = View.VISIBLE
            mAuth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        getProfile(user)
                    } else {
                        progressBar.visibility = View.GONE
                        Commons.showAlertDialog(
                            context = this@LoginActivity,
                            message = getString(R.string.email_not_exist)
                        )
                    }
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

    private fun getProfile(user: FirebaseUser?) {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        user?.uid?.let {
            mDatabaseReference.child("other_user_details").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    progressBar.visibility = View.GONE
                    val userProfile = dataSnapshot.getValue<UserProfile>(UserProfile::class.java)
                    if (userType == userProfile?.role) {
                        editor.run {
                            putString(Constants.user, userType)
                            putString(Constants.role, userProfile.role)
                            putString(Constants.email, etEmail.text.toString())
                            putString(Constants.password, etPassword.text.toString())
                            putString(Constants.username, userProfile.userName)
                            putString(Constants.phonenumber, userProfile.phoneNumber)
                            putString(Constants.gender, userProfile.gender)
                            apply()
                        }

                        when (userType) {
                            Constants.admin -> {
                                callDashboardScreen(Intent(this@LoginActivity, AdminDashboardActivity::class.java))
                            }

                            Constants.studnet -> {
                                callDashboardScreen(Intent(this@LoginActivity, StudentDashboardActivity::class.java))
                            }

                            Constants.teacher -> {
                                callDashboardScreen(Intent(this@LoginActivity, TeacherDashboardActivity::class.java))
                            }
                        }

                    } else {
                        Commons.showAlertDialog(
                            context = this@LoginActivity,
                            message = "${getString(R.string.please_select)} ${userProfile?.role} ${getString(R.string.login_page)}"
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    progressBar.visibility = View.GONE
                    Log.d(
                        ProfileActivity::class.java.simpleName, databaseError.toString()
                    )
                }
            })
        }
    }

    //Clearing all existing screens and moving to dashboard screen
    private fun callDashboardScreen(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        finishAffinity()
        startActivity(intent)
    }
}

fun CharSequence.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

