package com.venkatesh.schoolmanagement.activity.admin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.ServiceAddUser
import com.venkatesh.schoolmanagement.activity.isValidEmail
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_create_student.*

//This class is used to create student admin teacher

class CreateStudentTeacherActivity : AppCompatActivity() {
    private var gender = ""
    lateinit var mAuth: FirebaseAuth
    lateinit var user: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_student)
        mAuth = FirebaseAuth.getInstance()

        user = intent.getStringExtra(Constants.user)

        sharedPreferences = getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user.let {
            when (user) {
                Constants.teacher -> {
                    title = "${getString(R.string.btn_add_student)} ${Constants.teacher}"
                }

                Constants.studnet -> {
                    title = "${getString(R.string.btn_add_student)} ${Constants.studnet}"
                }
            }
        }
        rgAddGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbAddMale -> {
                    gender = rbAddMale.text.toString()
                }

                R.id.rbAddFemale -> {
                    gender = rbAddFemale.text.toString()
                }
            }
        }

        btnAdd.setOnClickListener {
            if (!validateDetails()) {
                progressBarAdd.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(etAddEmail.text.toString(), etAddPassword.text.toString())
                    .addOnCompleteListener(
                        this@CreateStudentTeacherActivity
                    ) { task ->
                        if (task.isSuccessful) {
                            addOtherDetails(mAuth.currentUser)
                        } else {
                            Commons.showAlertDialog(
                                context = this@CreateStudentTeacherActivity,
                                message = getString(R.string.user_creation_fail)
                            )

                        }
                    }

            }
        }
    }

    //Firebase Creating user
    private fun addOtherDetails(currentUser: FirebaseUser?) {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        currentUser?.uid?.let {
            val profileData = getProfileDataFromUi(it)
            mDatabaseReference.child("other_user_details").child(it).setValue(profileData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Commons.showAlertDialog(
                            context = this@CreateStudentTeacherActivity,
                            message = " $user ${getString(R.string.profile_create_success)}\n\n ${getString(R.string.email)} :${etAddEmail.text} \n ${getString(
                                R.string.password
                            )} :${etAddPassword.text}",
                            dialogCallback = object : DialogCallback() {
                                override fun positiveClick() {
                                    mAuth.signOut()
                                    reSignIn()
                                    user.let {
                                        when (user) {
                                            Constants.teacher -> {
                                                ServiceAddUser.addTeacher(
                                                    this@CreateStudentTeacherActivity,
                                                    profileData
                                                )
                                            }

                                            Constants.studnet -> {
                                                ServiceAddUser.addStudent(
                                                    this@CreateStudentTeacherActivity,
                                                    profileData
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        Commons.showAlertDialog(
                            context = this@CreateStudentTeacherActivity,
                            message = getString(R.string.profile_update_failed)
                        )
                    }
                }
        }
    }

    //Resigning into original signin
    private fun reSignIn() {
        runOnUiThread {
            mAuth.signInWithEmailAndPassword(
                sharedPreferences.getString(Constants.email, ""),
                sharedPreferences.getString(Constants.password, "")
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        progressBarAdd.visibility = View.GONE
                        finish()
                    }
                }
        }

    }

    //Creating model object with profile
    private fun getProfileDataFromUi(uid: String): UserProfile {
        return UserProfile(etAddName.text.toString(), etAddMobileNumber.text.toString(), gender, user, uid, "")
    }

    //Validations
    private fun validateDetails(): Boolean {
        var status = false
        if (etAddEmail.text.isEmpty() || !(etAddEmail.text.isValidEmail())) {
            status = true
            Commons.showAlertDialog(
                context = this@CreateStudentTeacherActivity,
                message = getString(R.string.valid_email)
            )
        } else if (etAddPassword.text.isEmpty() || etAddPassword.text.length <= 6) {
            status = true
            Commons.showAlertDialog(
                context = this@CreateStudentTeacherActivity,
                message = getString(R.string.valid_password)
            )
        } else if (etAddName.text.isEmpty() || etAddName.text.length <= 6) {
            status = true
            Commons.showAlertDialog(
                context = this@CreateStudentTeacherActivity,
                message = getString(R.string.valid_name)
            )
        } else if (etAddMobileNumber.text.isEmpty() || etAddMobileNumber.text.length < 10) {
            status = true
            Commons.showAlertDialog(
                context = this@CreateStudentTeacherActivity,
                message = getString(R.string.valid_mobile)
            )
        }
        return status
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return true
    }
}
