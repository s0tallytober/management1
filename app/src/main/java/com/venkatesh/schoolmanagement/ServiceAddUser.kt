package com.venkatesh.schoolmanagement

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Constants

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_ADD_USER = "com.venkatesh.schoolmanagement.action.ADD_USER"
private const val ACTION_BAZ = "com.venkatesh.schoolmanagement.action.BAZ"

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
            ACTION_ADD_USER -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1) as UserProfile
                addUser(param1)
            }
        }
    }

    /*
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun addUser(param1: UserProfile) {
        FirebaseDatabase.getInstance().reference.child(Constants.students).push().setValue(param1)
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
        fun addUser(context: Context, userProfile: UserProfile) {
            val intent = Intent(context, ServiceAddUser::class.java).apply {
                action = ACTION_ADD_USER
                Bundle().apply {
                    putSerializable(EXTRA_PARAM1, userProfile)
                }

            }
            context.startService(intent)
        }

    }
}
