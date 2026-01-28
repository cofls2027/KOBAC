package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("details")
    val details: Map<String, Any>? = null
)
