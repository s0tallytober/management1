package com.venkatesh.schoolmanagement.model

import com.google.gson.annotations.SerializedName

data class SMSEvent(
    @SerializedName("eventImageUrl")
    val eventImageUrl: String = "",
    @SerializedName("eventMessage")
    val eventMessage: String = "",
    @SerializedName("eventTime")
    val eventTime: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userId")
    val userId: String = ""
)