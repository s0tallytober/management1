package com.venkatesh.schoolmanagement.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_select_login_type.*

class SelectLoginTypeActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_login_type)

        title=getString(R.string.select_login_type)

        btnAdmin.setOnClickListener(this)
        btnTeacher.setOnClickListener(this)
        btnStudent.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val loginIntent = Intent(this@SelectLoginTypeActivity, LoginActivity::class.java)
        when (v?.id) {

            R.id.btnAdmin -> {
                loginIntent.putExtra(Constants.loginType, Constants.admin)
            }

            R.id.btnTeacher -> {
                loginIntent.putExtra(Constants.loginType, Constants.teacher)
            }

            R.id.btnStudent -> {
                loginIntent.putExtra(Constants.loginType, Constants.studnet)
            }
        }

        startActivity(loginIntent)

    }
}
