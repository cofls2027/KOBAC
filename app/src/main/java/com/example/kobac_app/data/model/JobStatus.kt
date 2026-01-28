package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

enum class JobStatusType {
    @SerializedName("PENDING")
    PENDING,
    
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS,
    
    @SerializedName("COMPLETED")
    COMPLETED,
    
    @SerializedName("FAILED")
    FAILED
}

data class JobStatus(
    @SerializedName("jobId")
    val jobId: String,
    
    @SerializedName("status")
    val status: JobStatusType,
    
    @SerializedName("progress")
    val progress: Int? = null, // 0-100
    
    @SerializedName("result")
    val result: Map<String, Any>? = null,
    
    @SerializedName("error")
    val error: ApiError? = null
)
