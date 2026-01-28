package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String
)
