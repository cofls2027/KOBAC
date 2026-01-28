package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: T?,
    
    @SerializedName("error")
    val error: ApiError?,
    
    @SerializedName("traceId")
    val traceId: String?
)
