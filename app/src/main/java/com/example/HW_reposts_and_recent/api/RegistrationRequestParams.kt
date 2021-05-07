package com.example.HW_reposts_and_recent.api

import com.google.gson.annotations.SerializedName

data class RegistrationRequestParams(
    @SerializedName("userName")
    val username: String,
    @SerializedName("password")
    val password: String
)