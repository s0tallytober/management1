package com.venkatesh.schoolmanagement.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.Constants.RESULT_LOAD_IMAGE
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_profile.*


//Update or Add Profile
class ProfileActivity : AppCompatActivity() {
    private var gender = ""
    private var filePath: Uri? = null
    private var userProfile: UserProfile? = null
    private var downloadUrl: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)
        displayProfile()

        btnUploadProfileImage.setOnClickListener {
            profileImageUpload()
        }
    }

    //Uploading Image
    private fun profileImageUpload() {
        // val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission has already been granted
            callImagepPicker()
        } else {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@ProfileActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@ProfileActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private fun callImagepPicker() {
        val imgIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(imgIntent, Constants.RESULT_LOAD_IMAGE)
    }

    override fun onStart() {
        super.onStart()

        //Listener for gender radio button selection
        rgGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbMale -> {
                    gender = rbMale.text.toString()
                }

                R.id.rbFemale -> {
                    gender = rbFemale.text.toString()
                }
            }
        }

        btnUpdateProfile.setOnClickListener {
            getProfileDataFromUi()?.let { it1 -> updateProfile(it1) }
        }
    }

    //updating profile attached to ui screen
    private fun updateProfile(userProfile: UserProfile) {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        user?.uid?.let {
            mDatabaseReference.child("other_user_details").child(it).setValue(userProfile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideProgressBar()
                        Commons.showAlertDialog(
                            context = this@ProfileActivity,
                            message = getString(R.string.profile_update_success),
                            dialogCallback = object : DialogCallback() {
                                override fun positiveClick() {
                                    finish()
                                }
                            }
                        )
                    } else {
                        hideProgressBar()
                        Commons.showAlertDialog(
                            context = this@ProfileActivity,
                            message = getString(R.string.profile_update_failed)
                        )
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

    private fun getProfileDataFromUi(): UserProfile? {
        return FirebaseAuth.getInstance().currentUser?.uid?.let {
            UserProfile(
                etName.text.toString(),
                etMobileNumber.text.toString(),
                gender,
                sharedPreferences.getString(Constants.role, ""),
                it,
                downloadUrl + ""
            )
        }
    }

    private fun displayProfile() {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser

        user?.uid?.let {
            mDatabaseReference.child("other_user_details").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userProfile = dataSnapshot.getValue<UserProfile>(UserProfile::class.java)
                    updateUi(userProfile)
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

    private fun updateUi(userProfile: UserProfile?) {
        this.userProfile = userProfile
        userProfile.let {
            etName.setText(it?.userName)
            etMobileNumber.setText(it?.phoneNumber)
            if (it?.gender == getString(R.string.male)) {
                rbMale.isChecked = true
            } else {
                rbFemale.isChecked = true
            }

            Glide.with(this).load(userProfile?.url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                filePath = it.data
                uploadImage(filePath)
                try {
                    //getting image from gallery
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    //Setting image to ImageView
                    imgProfile.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun uploadImage(filePath: Uri?) {
        showProgressBar()
        val mStorageReference = FirebaseStorage.getInstance().getReference(Constants.profile_images)
        /* filePath?.let {
             val fileReference =
                 mStorageReference.child("${System.currentTimeMillis()}.${getExtensionFromUri(it)}")

             fileReference.putFile(it)
                 .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                     // Get a URL to the uploaded content
                     downloadUrl = fileReference.downloadUrl.result.toString()
                     getProfileDataFromUi()?.let { it1 -> updateProfile(it1) }
                 })
                 .addOnFailureListener(OnFailureListener {
                     // Handle unsuccessful uploaimgProfileds
                     Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                 })

         }*/

        filePath?.let {
            val fileReference =
                mStorageReference.child("${System.currentTimeMillis()}.${getExtensionFromUri(it)}")
            val uploadTask = fileReference.putFile(it)
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri>? {
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return fileReference.downloadUrl
                }
            }).addOnCompleteListener {
                progressBarCP.visibility=View.GONE
                downloadUrl = it.result.toString()
                //getProfileDataFromUi()?.let { it1 -> updateProfile(it1) }
            }
        }

    }

    private fun getExtensionFromUri(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    callImagepPicker()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Commons.showAlertDialog(
                        this@ProfileActivity,
                        message = getString(R.string.need_read_store_perm)
                    )
                }
                return
            }
        }
    }
}
