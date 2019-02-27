package com.venkatesh.schoolmanagement.activity.admin

import android.os.Bundle
import android.support.annotation.WorkerThread
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

        displayProfile()
    }

    private fun creatingActionsInGridView() {
        val actionsList =
            arrayListOf("Add Student", "Add Teacher", "Remove Student", "Remove Teacher", "Add Course", "Add Event")
        val adapter = AdminDashboardAdapter(this, actionsList)
        recyclerAdminDashboard.layoutManager = GridLayoutManager(this@AdminDashboardActivity, 2)
        recyclerAdminDashboard.adapter = adapter
    }

    /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.admin_dashboard_menu, menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         when (item?.itemId) {
             R.id.logout -> {
                 Commons.showAlertDialog(
                     context = this@AdminDashboardActivity,
                     nofOptions = 2,
                     positiveButtonText = getString(R.string.yes),
                     message = getString(R.string.msgLogout),
                     dialogCallback = object : DialogCallback() {
                         override fun positiveClick() {
                             signOut()
                             val intent = Intent(this@AdminDashboardActivity, SelectLoginTypeActivity::class.java)
                             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                             intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                             finishAffinity()
                             startActivity(intent)
                         }
                     }
                 )
             }

             R.id.chgPassword -> {
                 val intent = Intent(this@AdminDashboardActivity, ChangePasswordActivity::class.java)
                 startActivity(intent)
             }

             R.id.updateProfile -> {
                 val intent = Intent(this@AdminDashboardActivity, ProfileActivity::class.java)
                 startActivity(intent)
             }
         }
         return true
     }*/

    //sign out method
    override fun signOut() {
        mAuth.signOut()
    }

    @WorkerThread
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
