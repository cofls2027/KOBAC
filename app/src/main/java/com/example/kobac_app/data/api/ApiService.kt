package com.example.kobac_app.data.api

import com.example.kobac_app.data.model.ApiResponse
import com.example.kobac_app.data.model.ConnectRequest
import com.example.kobac_app.data.model.ConnectResponse
import com.example.kobac_app.data.model.LoginRequest
import com.example.kobac_app.data.model.LoginResponse
import com.example.kobac_app.data.model.PortfolioResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
    
    @POST("mydata/connect")
    suspend fun connectMyData(@Body request: ConnectRequest): ApiResponse<ConnectResponse>
    
    @GET("mydata/portfolio")
    suspend fun getPortfolio(): PortfolioResponse
}
