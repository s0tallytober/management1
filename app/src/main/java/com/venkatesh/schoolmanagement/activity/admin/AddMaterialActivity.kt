package com.venkatesh.schoolmanagement.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.MaterialUpload
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Commons.getExtensionFromUri
import com.venkatesh.schoolmanagement.utilities.Constants
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_add_material.*

class AddMaterialActivity : AppCompatActivity() {
    var selected: String? = null
    private var fileUrl: Uri? = null
    private var downloadUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_material)
        if (intent.hasExtra(Constants.materials)) {
            selected = intent.getStringExtra(Constants.materials)
            title = selected
        } else
            title = getString(R.string.add_material)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myStrings = Constants.getClasses(this)

        //Adapter for spinner
        mySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)

        //item selected listener for spinner
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected = myStrings[p2]
            }
        }
    }

    override fun onStart() {
        super.onStart()
        uploadBtn.setOnClickListener {
            getPermissions()
        }

        btnAddMaterial.setOnClickListener {
            val message = validations()
            if (message == "") {
                uploadDocument()
            } else {
                Commons.showAlertDialog(context = this, message = message)
            }
        }
    }

    private fun validations(): String {
        return if (mySpinner.visibility == View.VISIBLE && selected == getString(R.string.choose_class)) {
            getString(R.string.select_class)
        } else if (fileName.text.toString() == "") {
            getString(R.string.select_file)
        } else if (etAddDescription.text.toString() == "") {
            getString(R.string.enter_desc)
        } else {
            ""
        }
    }

    private fun showProgressBar() {
        progressBarCP.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarCP.visibility = View.GONE
    }


    private fun addMaterial() {

        var tableName = ""
        val data = arrayListOf<MaterialUpload>()
        when (selected) {
            getString(R.string.sec_class) -> {
                tableName = getString(R.string.sec_class)
                data.addAll(Constants.secMaterial)
            }
            getString(R.string.third_class) -> {
                tableName = getString(R.string.third_class)
                data.addAll(Constants.thirdMaterial)
            }
            getString(R.string.four_class) -> {
                tableName = getString(R.string.four_class)
                data.addAll(Constants.fourthMaterial)
            }
            getString(R.string.five_class) -> {
                tableName = getString(R.string.five_class)
                data.addAll(Constants.fifthMaterial)
            }
            getString(R.string.six_class) -> {
                tableName = getString(R.string.six_class)
                data.addAll(Constants.sixthMaterial)
            }
            getString(R.string.seven_class) -> {
                tableName = getString(R.string.seven_class)
                data.addAll(Constants.seventhMaterial)
            }
            getString(R.string.eight_class) -> {
                tableName = getString(R.string.eight_class)
                data.addAll(Constants.eightMaterial)
            }
            getString(R.string.nine_class) -> {
                tableName = getString(R.string.nine_class)
                data.addAll(Constants.nineMaterial)
            }
            getString(R.string.ten_class) -> {
                tableName = getString(R.string.ten_class)
                data.addAll(Constants.tenMaterial)
            }
            else -> {
                tableName = getString(R.string.first_class)
                data.addAll(Constants.firstMaterial)
            }
        }
        val event = MaterialUpload(
            downloadUrl.toString(),
            etAddDescription.text.toString(),
            Commons.getCurrentDateTime()!!,
            Constants.userProfile!!.userName,
            Constants.userProfile!!.userId,
            selected.toString()
        )
        data.add(event)
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabaseReference.child(Constants.materials).child(tableName).setValue(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    hideProgressBar()
                    Commons.showAlertDialog(
                        context = this@AddMaterialActivity,
                        message = getString(R.string.file_update_success),
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
                        context = this@AddMaterialActivity,
                        message = getString(R.string.file_update_failed)
                    )
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
                    callFilePicker()
                }
            }
        }
    }

    private fun callFilePicker() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        startActivityForResult(intent, 301)
    }

    private fun getPermissions() {
        // val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission has already been granted
            callFilePicker()
        } else {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AddMaterialActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@AddMaterialActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == 301 && resultCode == RESULT_OK) {
                fileUrl = it.data
                fileName.setText(fileUrl.toString())
            }
        }
    }

    private fun uploadDocument() {
        showProgressBar()
        val mStorageReference = FirebaseStorage.getInstance().getReference(Constants.materials)
        fileUrl?.let {
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
                addMaterial()
            }
        }
    }
}
