package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class ConnectRequest(
    @SerializedName("mock_token")
    val mockToken: String,
    
    @SerializedName("user_search_id")
    val userSearchId: String,
    
    @SerializedName("crypto_addresses")
    val cryptoAddresses: Map<String, String> = emptyMap()
)
