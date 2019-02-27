package com.venkatesh.schoolmanagement.utilities

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
    const val chats = "chat_history"
    var userProfile: UserProfile? = null
    const val profile_images = "profile_images"
    const val events = "events"
    const val event_images="event_images"
}