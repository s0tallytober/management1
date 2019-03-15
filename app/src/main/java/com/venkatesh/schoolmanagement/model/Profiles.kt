package com.venkatesh.schoolmanagement.model

import java.io.Serializable

class Profiles:Serializable {
    var userId: String = ""
    var userInfo: UserInfo? = null
}

class UserInfo:Serializable {
    var name: String = ""
    var phoneNumber = ""
    var gender: String = ""
    var email: String = ""
    var role: String = ""
    var url: String = ""
    var className: String = ""
}