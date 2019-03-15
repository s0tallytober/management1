package com.venkatesh.schoolmanagement.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Commons.getExtensionFromUri
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_add_event.*

class AddEventActivity : AppCompatActivity() {

    private var filePath: Uri? = null
    private var downloadUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        title = getString(R.string.add_event)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        btnAddEvent.setOnClickListener {
            uploadImage(filePath)
        }
        addImage.setOnClickListener {
            profileImageUpload()
        }
    }

    private fun profileImageUpload() {
        // val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission has already been granted
            callImagePicker()
        } else {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AddEventActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@AddEventActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    callImagePicker()
                }
            }
        }
    }

    private fun callImagePicker() {
        val imgIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(imgIntent, Constants.RESULT_LOAD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                filePath = it.data
                try {
                    //getting image from gallery
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    //Setting image to ImageView
                    icEventImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showProgressBar() {
        progressBarCP.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarCP.visibility = View.GONE
    }


    private fun uploadImage(filePath: Uri?) {
        showProgressBar()
        val mStorageReference = FirebaseStorage.getInstance().getReference(Constants.event_images)
        filePath?.let {
            val fileReference =
                mStorageReference.child("${System.currentTimeMillis()}.${getExtensionFromUri(this, it)}")
            val uploadTask = fileReference.putFile(it)
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri>? {
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return fileReference.downloadUrl
                }
            }).addOnCompleteListener {
                downloadUrl = it.result.toString()
                addEvent()
                //getProfileDataFromUi()?.let { it1 -> updateProfile(it1) }
            }
        }

    }

    private fun addEvent() {
        val event = SMSEvent(
            downloadUrl.toString(), etAddEventMessage.text.toString(),
            Commons.getCurrentDateTime()!!,
            Constants.userProfile!!.userName,
            Constants.userProfile!!.userId
        )
        Constants.eventsList.add(event)
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabaseReference.child(Constants.events).setValue(Constants.eventsList)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    hideProgressBar()
                    Commons.showAlertDialog(
                        context = this@AddEventActivity,
                        message = getString(R.string.event_update_success),
                        dialogCallback = object : DialogCallback() {
                            override fun positiveClick() {
                                val intent = Intent()
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                        }
                    )
                } else {
                    hideProgressBar()
                    Commons.showAlertDialog(
                        context = this@AddEventActivity,
                        message = getString(R.string.event_update_failed)
                    )
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
