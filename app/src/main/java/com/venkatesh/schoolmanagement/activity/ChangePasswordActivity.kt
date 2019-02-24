package com.venkatesh.schoolmanagement.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_change_password.*

//Change logged user password
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        title = getString(R.string.changePassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get firebase auth instance
        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()

        //get current user
        val user = FirebaseAuth.getInstance().currentUser

        btnChgPassword.setOnClickListener {
            if (user != null) {
                if (etNewPassword.text.isEmpty() || etConfNewPassword.text.isEmpty() || etNewPassword.text.length < 6 || etConfNewPassword.text.length < 6) {
                    Commons.showAlertDialog(
                        context = this@ChangePasswordActivity,
                        message = getString(R.string.valid_password)
                    )
                } else if (etNewPassword.text.toString() != etConfNewPassword.text.toString()) {
                    Commons.showAlertDialog(
                        context = this@ChangePasswordActivity,
                        message = getString(R.string.password_notmatching)
                    )

                } else {
                    progressBarCP.visibility = View.VISIBLE
                    user.updatePassword(etConfNewPassword.text.toString())
                        .addOnCompleteListener({ task ->
                            if (task.isSuccessful) {
                                Commons.showAlertDialog(
                                    context = this@ChangePasswordActivity,
                                    message = getString(R.string.password_change_success)
                                )
                                progressBarCP.visibility = View.GONE
                            } else {
                                progressBarCP.visibility = View.GONE
                                Commons.showAlertDialog(
                                    context = this@ChangePasswordActivity,
                                    message = getString(R.string.password_update_fail),dialogCallback = object :
                                        DialogCallback() {
                                        override fun positiveClick() {
                                            finish()
                                        }
                                    }
                                )
                            }
                        })
                }
            }
        }
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
