package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

// 요청 데이터 클래스
data class BlockchainScanRequest(
    val address: String,
    val chain: String = "auto"
)

// 응답 데이터 클래스
data class BlockchainScanResponse(
    val address: String,
    val chains: List<String>,
    val assets: List<ScannedAsset>,
    val assetCount: Int,
    val totalValueUsd: Double,
    val totalValueKrw: Double
)

data class ScannedAsset(
    val chain: String,
    val symbol: String,
    val decimals: Int,
    val balance: Double,
    @SerializedName("assetType")
    val assetType: String,
    @SerializedName("assetId")
    val assetId: String,
    val balanceRaw: String,
    val priceUsd: Double,
    val priceKrw: Double,
    val valueUsd: Double,
    val valueKrw: Double,
    @SerializedName("pricesTs")
    val pricesTs: String
)
