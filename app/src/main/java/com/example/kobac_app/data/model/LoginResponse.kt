package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

/** API 응답 래퍼: { "success", "data", "error", "traceId" } */
data class LoginApiResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: LoginResponse,
    
    @SerializedName("error")
    val error: String? = null,
    
    @SerializedName("traceId")
    val traceId: String? = null
)

/** 로그인 성공 시 data 내부 객체 */
data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    
    @SerializedName("expiresInSec")
    val expiresInSec: Int,
    
    @SerializedName("user")
    val user: User
)
