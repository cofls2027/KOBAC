package com.example.kobac_app.data.api

import com.example.kobac_app.data.model.ApiResponse
import com.example.kobac_app.data.model.LoginRequest
import com.example.kobac_app.data.model.LoginResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
    
    // TODO: 실제 API 엔드포인트에 맞게 수정 필요
    // 예시:
    // @GET("accounts")
    // suspend fun getAccounts(): ApiResponse<List<Account>>
    
    // @POST("accounts/connect")
    // suspend fun connectAccount(@Body request: ConnectAccountRequest): ApiResponse<Account>
}
