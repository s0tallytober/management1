package com.venkatesh.schoolmanagement.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.BaseActivityToChildActivity
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.DialogCallback

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    lateinit var baseContext: BaseActivityToChildActivity
    lateinit var sharedPreferences: SharedPreferences

    override fun onStart() {
        super.onStart()
        sharedPreferences = getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)

        title = "${sharedPreferences.getString(Constants.username, "")} ( ${sharedPreferences.getString(
            Constants.role,
            ""
        )} )" // Tool bar title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                Commons.showAlertDialog(
                    context = this@BaseActivity,
                    nofOptions = 2,
                    positiveButtonText = getString(R.string.yes),
                    message = getString(R.string.msgLogout),
                    dialogCallback = object : DialogCallback() {
                        override fun positiveClick() {
                            sharedPreferences.edit().clear()
                            baseContext.signOut()
                            val intent = Intent(this@BaseActivity, SelectLoginTypeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            finishAffinity()
                            startActivity(intent)
                        }
                    }
                )
            }

            R.id.chgPassword -> {
                val intent = Intent(this@BaseActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.updateProfile -> {
                val intent = Intent(this@BaseActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.groupChat -> {
                val intent = Intent(this@BaseActivity, ChatActivity::class.java)
                startActivity(intent)
            }

            R.id.deleteAccount -> {
                Commons.showAlertDialog(this@BaseActivity,
                    nofOptions = 2,
                    positiveButtonText = getString(R.string.yes),
                    negativeButtonText = getString(R.string.cancel),
                    message = getString(R.string.areYouSure), dialogCallback = object : DialogCallback() {
                        override fun positiveClick() {
                            FirebaseAuth.getInstance().currentUser?.delete()
                                ?.addOnCompleteListener(OnCompleteListener<Void> { task ->
                                    if (task.isSuccessful) {
                                        Commons.showAlertDialog(
                                            context = this@BaseActivity,
                                            message = getString(R.string.account_delete_message)
                                        )
                                        startActivity(
                                            Intent(
                                                this@BaseActivity,
                                                SelectLoginTypeActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@BaseActivity,
                                            "Failed to delete your account!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    })
            }
        }
        return true
    }

    fun attachInstance(activity: BaseActivityToChildActivity) {
        baseContext = activity
    }

}