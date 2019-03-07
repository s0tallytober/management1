package com.venkatesh.schoolmanagement

import android.app.Activity
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.DialogCallback

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_ADD_STUDENT = "com.venkatesh.schoolmanagement.action.ADD_STUDENT"
private const val ACTION_ADD_TEACHER = "com.venkatesh.schoolmanagement.action.ADD_TEACHER"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.venkatesh.schoolmanagement.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.venkatesh.schoolmanagement.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class ServiceAddUser : IntentService("ServiceAddUser") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_ADD_STUDENT -> {
                val param1 = intent.getSerializableExtra(EXTRA_PARAM1) as UserProfile
                addStudent(param1, Constants.students)
            }

            ACTION_ADD_TEACHER -> {
                val param1 = intent.getSerializableExtra(EXTRA_PARAM1) as UserProfile
                addTeacher(param1, Constants.teacher)
            }
        }
    }

    /*
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun addStudent(param1: UserProfile, tabelName: String) {
/*
        FirebaseDatabase.getInstance().reference.child(tabelName).push().setValue(param1)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User added success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "User added fail", Toast.LENGTH_SHORT).show()
                }
            }
*/
        Constants.studentsData.add(param1)
        FirebaseDatabase.getInstance().reference.child(tabelName).setValue(Constants.studentsData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User added success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "User added fail", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addTeacher(param1: UserProfile, tabelName: String) {
/*
        FirebaseDatabase.getInstance().reference.child(tabelName).push().setValue(param1)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User added success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "User added fail", Toast.LENGTH_SHORT).show()
                }
            }
*/
        Constants.teachersData.add(param1)
        FirebaseDatabase.getInstance().reference.child(tabelName).setValue(Constants.teachersData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User added success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "User added fail", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun addStudent(context: Context, userProfile: UserProfile) {
            val intent = Intent(context, ServiceAddUser::class.java)

            intent.action = ACTION_ADD_STUDENT
            intent.putExtras(Bundle().apply {
                putSerializable(EXTRA_PARAM1, userProfile)
            })
            context.startService(intent)
        }

        @JvmStatic
        fun addTeacher(context: Context, userProfile: UserProfile) {
            val intent = Intent(context, ServiceAddUser::class.java)

            intent.action = ACTION_ADD_TEACHER
            intent.putExtras(Bundle().apply {
                putSerializable(EXTRA_PARAM1, userProfile)
            })
            context.startService(intent)
        }
    }
}
