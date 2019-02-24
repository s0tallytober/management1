package com.venkatesh.schoolmanagement.activity.teacher

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.BaseActivityToChildActivity
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.BaseActivity

class TeacherDashboardActivity : BaseActivity(), BaseActivityToChildActivity {
    override fun signOut() {

    }

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)
        super.attachInstance(this)
        mAuth = FirebaseAuth.getInstance()
        title = getString(R.string.teacher_dashboard)
    }
}
