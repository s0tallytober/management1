package com.venkatesh.schoolmanagement.activity.admin

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.Profiles
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_profile_details.*

class ProfileDetailsActivity : AppCompatActivity() {
    private lateinit var profile: Profiles
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)
        title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra(Constants.profile)) {
            val profile = intent.getSerializableExtra(Constants.profile) as Profiles
            this.profile = profile
            if (profile.userInfo?.role != Constants.studnet) {
                fabAttachStudent.visibility = View.GONE
            }
            addDataToViews(profile)
        }
    }

    private fun addDataToViews(profile: Profiles) {
        profile.userInfo?.let {
            tvName.text = it.name
            tvPhone.text = it.phoneNumber
            tvGender.text = it.gender
            tvClassName.text = it.className
            if (it.url!=null && it.url.isNotEmpty()){
                Glide.with(this).load(it.url)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile)
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

    override fun onStart() {
        super.onStart()
        fabAttachStudent.setOnClickListener {
            val b = AlertDialog.Builder(this)
            b.setTitle(getString(R.string.select_update_class))
            val types: Array<String> = Constants.getClasses(this)
            b.setItems(types) { dialog, which ->
                dialog.dismiss()
                addOtherDetails(types[which])
            }
            b.show()
        }
    }

    private fun addOtherDetails(s: String) {
        val mDatabaseReference = FirebaseDatabase.getInstance().reference
        val profileData = getProfileData(s)
        mDatabaseReference.child("other_user_details").child(profile.userId).setValue(profileData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Commons.showAlertDialog(
                        context = this@ProfileDetailsActivity,
                        message = getString(R.string.update_class_student)
                    )
                } else {
                    Commons.showAlertDialog(
                        context = this@ProfileDetailsActivity,
                        message = getString(R.string.profile_update_failed)
                    )
                }
            }
    }

    private fun getProfileData(classNam: String): UserProfile {
        val userProfile = UserProfile()
        userProfile.run {
            userName = profile.userInfo?.name!!
            gender = profile.userInfo?.gender!!
            phoneNumber = profile.userInfo?.phoneNumber!!
            url = profile.userInfo?.url!!
            role = profile.userInfo?.role!!
            userId = profile.userId
            className = classNam
        }
        return userProfile
    }

}
