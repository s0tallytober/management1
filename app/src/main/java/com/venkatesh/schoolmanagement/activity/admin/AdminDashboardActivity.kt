package com.venkatesh.schoolmanagement.activity.admin

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.BaseActivityToChildActivity
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.BaseActivity
import com.venkatesh.schoolmanagement.adapter.AdminDashboardAdapter
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboardActivity : BaseActivity(), BaseActivityToChildActivity {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        mAuth = FirebaseAuth.getInstance()
        super.attachInstance(this)
        creatingActionsInGridView() // Preparing list of actions on main screen
    }

    private fun creatingActionsInGridView() {
        val actionsList =
            arrayListOf(
                getString(R.string.student),
                getString(R.string.teacher),
                getString(R.string.addAdmin),
                getString(R.string.materials),
                getString(R.string.addCource),
                getString(R.string.events)
            )
        val adapter = AdminDashboardAdapter(this, actionsList)
        recyclerAdminDashboard.layoutManager = GridLayoutManager(this@AdminDashboardActivity, 2)
        recyclerAdminDashboard.adapter = adapter
    }

    //sign out method
    override fun signOut() {
        mAuth.signOut()
    }


}
