package com.venkatesh.schoolmanagement.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*

class SplashActivity : Activity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

    }

    override fun onResume() {
        super.onResume()
        /*Handler().postDelayed({
            checkPreviousLoginSession()
        }, 2000)*/
        checkPreviousLoginSession()
    }

    private fun checkPreviousLoginSession() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            Handler().postDelayed({startActivity(Intent(this@SplashActivity, SelectLoginTypeActivity::class.java))
                finish()
                checkPreviousLoginSession()
            }, 2000)
        } else {
            getProfile(user)
        }
    }

    private fun getProfile(user: FirebaseUser?) {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        user?.uid?.let {
            mDatabaseReference.child("other_user_details").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userProfile = dataSnapshot.getValue<UserProfile>(UserProfile::class.java)
                    Constants.userProfile = dataSnapshot.getValue<UserProfile>(UserProfile::class.java)
                    editor.run {
                        putString(Constants.role, userProfile?.role)
                        putString(Constants.username, userProfile?.userName)
                        putString(Constants.phonenumber, userProfile?.phoneNumber)
                        putString(Constants.gender, userProfile?.gender)
                        apply()
                    }

                    when (userProfile?.role) {
                        Constants.admin -> {
                            startActivity(Intent(this@SplashActivity, AdminDashboardActivity::class.java))
                        }

                        Constants.studnet -> {
                            startActivity(Intent(this@SplashActivity, StudentDashboardActivity::class.java))
                        }

                        Constants.teacher -> {
                            startActivity(Intent(this@SplashActivity, TeacherDashboardActivity::class.java))
                        }
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


}
