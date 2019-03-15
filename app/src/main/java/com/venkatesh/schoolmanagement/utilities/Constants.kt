package com.venkatesh.schoolmanagement.utilities

import android.content.Context
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.MaterialUpload
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.model.UserProfile

object Constants {

    const val loginType = "LoginType"
    const val studnet = "Student"
    const val teacher = "Teacher"
    const val admin = "Admin"
    const val user = "user"
    const val sharedPrefName = "myappsharedpref"
    const val role = "role"
    const val email = "email"
    const val password = "password"
    const val username = "username"
    const val phonenumber = "phonenumber"
    const val gender = "gender"
    const val RESULT_LOAD_IMAGE = 100
    const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101
    const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 501
    const val chats = "chat_history"
    var userProfile: UserProfile? = null
    const val profile_images = "profile_images"
    const val events = "events"
    const val event_images = "event_images"
    var eventsList: ArrayList<SMSEvent> = arrayListOf()
    var firstMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var secMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var thirdMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var fourthMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var fifthMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var sixthMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var seventhMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var eightMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var nineMaterial: ArrayList<MaterialUpload> = arrayListOf()
    var tenMaterial: ArrayList<MaterialUpload> = arrayListOf()
    const val REQUEST_CODE = 101
    const val students = "students"
    var studentsData: ArrayList<UserProfile> = arrayListOf()
    var teachersData: ArrayList<UserProfile> = arrayListOf()

    const val materials = "materials"
    const val profile="profile"

    fun getClasses(context: Context): Array<String> {
        return arrayOf(
            context.getString(R.string.first_class),
            context.getString(R.string.sec_class),
            context.getString(R.string.third_class),
            context.getString(R.string.four_class),
            context.getString(R.string.five_class),
            context.getString(R.string.six_class),
            context.getString(R.string.seven_class),
            context.getString(R.string.eight_class),
            context.getString(R.string.nine_class),
            context.getString(R.string.ten_class)
        )

    }
}