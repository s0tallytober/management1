package com.venkatesh.schoolmanagement.model

import com.google.gson.annotations.SerializedName

data class MaterialUpload(
    @SerializedName("materialImageUrl")
    val materialImageUrl: String = "",
    @SerializedName("materialDescription")
    val materialDescription: String = "",
    @SerializedName("materialUploadTime")
    val materialUploadTime: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("className")
    val className: String = ""
)