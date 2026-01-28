package com.example.kobac_app.data.util

import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.ApiResponse
import com.example.kobac_app.data.model.JobStatus
import com.example.kobac_app.data.model.JobStatusType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object JobPollingUtil {
    /**
     * Job 상태를 주기적으로 폴링하는 Flow를 생성합니다.
     * 
     * @param apiService API 서비스 인스턴스
     * @param refreshJobId 조회할 Job ID
     * @param pollInterval 폴링 간격 (밀리초, 기본값: 500ms)
     * @param maxAttempts 최대 시도 횟수 (기본값: 60회 = 30초)
     * @return Job 상태를 방출하는 Flow
     */
    fun pollJobStatus(
        apiService: ApiService,
        refreshJobId: String,
        pollInterval: Long = 500,
        maxAttempts: Int = 60
    ): Flow<Result<JobStatus>> = flow {
        var attempts = 0
        
        while (attempts < maxAttempts) {
            try {
                // TODO: ApiService에 getJobStatus 메서드 추가 필요
                // val response: ApiResponse<JobStatus> = apiService.getJobStatus(refreshJobId)
                // 
                // if (response.success && response.data != null) {
                //     val jobStatus = response.data
                //     emit(Result.success(jobStatus))
                //     
                //     // Job이 완료되거나 실패하면 종료
                //     if (jobStatus.status == JobStatusType.COMPLETED || 
                //         jobStatus.status == JobStatusType.FAILED) {
                //         break
                //     }
                // } else {
                //     emit(Result.failure(Exception(response.error?.message ?: "Unknown error")))
                //     break
                // }
                
                delay(pollInterval)
                attempts++
            } catch (e: Exception) {
                emit(Result.failure(e))
                break
            }
        }
        
        // 최대 시도 횟수 초과 시 타임아웃
        if (attempts >= maxAttempts) {
            emit(Result.failure(Exception("Job polling timeout")))
        }
    }
}
