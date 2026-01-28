package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    
    @SerializedName("expiresInSec")
    val expiresInSec: Int,
    
    @SerializedName("user")
    val user: User
)
