package com.example.kobac_app.data.api

import com.example.kobac_app.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginApiResponse

    @POST("mydata/connect")
    suspend fun connectMyData(@Body connectRequest: ConnectRequest): ConnectResponse

    @GET("portfolio")
    suspend fun getPortfolio(): PortfolioResponse

    @POST("mydata/job/refresh")
    suspend fun refreshJob(@Header("Authorization") token: String): RefreshJobResponse
    
    @POST("blockchain/scan")
    suspend fun blockchainScan(@Body blockchainScanRequest: BlockchainScanRequest): BlockchainScanResponse
}
