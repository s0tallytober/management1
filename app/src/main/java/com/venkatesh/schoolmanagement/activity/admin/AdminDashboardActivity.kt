package com.venkatesh.schoolmanagement.activity.admin

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.venkatesh.schoolmanagement.BaseActivityToChildActivity
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.BaseActivity
import com.venkatesh.schoolmanagement.activity.ProfileActivity
import com.venkatesh.schoolmanagement.adapter.AdminDashboardAdapter
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboardActivity : BaseActivity(), BaseActivityToChildActivity {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        mAuth = FirebaseAuth.getInstance()
        super.attachInstance(this)
        creatingActionsInGridView() // Preparing list of actions on main screen

       // displayProfile()
    }

    private fun creatingActionsInGridView() {
        val actionsList =
            arrayListOf(
                getString(R.string.student),
                getString(R.string.teacher),
                getString(R.string.addAdmin),
                getString(R.string.add_material),
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


    private fun displayProfile() {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser

        user?.uid?.let {
            mDatabaseReference.child("other_user_details").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Constants.userProfile = dataSnapshot.getValue<UserProfile>(UserProfile::class.java)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(
                        ProfileActivity::class.java.simpleName, "Error trying to get classified ad for update " +
                                "" + databaseError
                    )
                }
            })
        }
    }
}
