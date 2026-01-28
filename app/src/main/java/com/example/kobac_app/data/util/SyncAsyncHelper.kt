package com.example.kobac_app.data.util

import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.ApiResponse
import com.example.kobac_app.data.model.RefreshJobResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 동기/비동기 혼합 방식으로 자산 조회/연동을 처리하는 헬퍼 클래스
 * 
 * 동기 방식: 캐시 또는 최근 데이터를 즉시 반환
 * 비동기 방식: refreshJobId를 발급하고 Job 상태를 조회하여 최신 데이터 갱신
 * 
 * SLA: 자산 조회/연동 작업은 3초 이내 응답 권장
 */
object SyncAsyncHelper {
    
    /**
     * 동기/비동기 혼합 방식으로 데이터를 조회합니다.
     * 
     * @param T 조회할 데이터 타입
     * @param apiService API 서비스 인스턴스
     * @param syncCall 동기 호출 함수 (캐시/최근 데이터 즉시 반환)
     * @param asyncRefreshCall 비동기 갱신 호출 함수 (refreshJobId 발급)
     * @param slaTimeout SLA 타임아웃 (밀리초, 기본값: 3000ms = 3초)
     * @return 동기 데이터와 비동기 갱신 상태를 포함하는 결과
     */
    suspend fun <T> fetchWithSyncAsync(
        apiService: ApiService,
        syncCall: suspend (ApiService) -> ApiResponse<T>,
        asyncRefreshCall: suspend (ApiService) -> ApiResponse<RefreshJobResponse>,
        slaTimeout: Long = 3000
    ): SyncAsyncResult<T> = withContext(Dispatchers.IO) {
        try {
            // 1. 동기 방식: 캐시 또는 최근 데이터 즉시 조회
            val syncResponse = syncCall(apiService)
            
            // 2. 비동기 방식: 백그라운드에서 refreshJobId 발급 및 갱신 시작
            val asyncJob = async {
                try {
                    val refreshResponse = asyncRefreshCall(apiService)
                    if (refreshResponse.success && refreshResponse.data != null) {
                        refreshResponse.data.refreshJobId
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    null
                }
            }
            
            // 3. SLA 타임아웃 내에 동기 응답 반환
            SyncAsyncResult(
                syncData = syncResponse.data,
                syncError = syncResponse.error,
                refreshJobId = asyncJob.await(),
                isSyncSuccess = syncResponse.success
            )
        } catch (e: Exception) {
            SyncAsyncResult(
                syncData = null,
                syncError = null,
                refreshJobId = null,
                isSyncSuccess = false,
                exception = e
            )
        }
    }
    
    /**
     * 동기/비동기 혼합 방식의 결과를 나타내는 데이터 클래스
     */
    data class SyncAsyncResult<T>(
        val syncData: T?,
        val syncError: com.example.kobac_app.data.model.ApiError?,
        val refreshJobId: String?,
        val isSyncSuccess: Boolean,
        val exception: Exception? = null
    ) {
        val hasSyncData: Boolean get() = syncData != null && isSyncSuccess
        val hasRefreshJob: Boolean get() = refreshJobId != null
    }
    
    /**
     * refreshJobId를 사용하여 비동기 갱신 상태를 폴링하는 Flow를 생성합니다.
     * 
     * @param refreshJobId 갱신 Job ID
     * @param apiService API 서비스 인스턴스
     * @return 갱신 상태를 방출하는 Flow
     */
    fun <T> observeAsyncRefresh(
        refreshJobId: String,
        apiService: ApiService
    ): Flow<Result<T>> = flow {
        // JobPollingUtil을 사용하여 Job 상태 폴링
        JobPollingUtil.pollJobStatus(apiService, refreshJobId).collect { jobStatusResult ->
            jobStatusResult.fold(
                onSuccess = { jobStatus ->
                    // TODO: JobStatus의 result를 T 타입으로 변환하는 로직 필요
                    // 현재는 예시로만 구현
                    // emit(Result.success(convertJobResultToData(jobStatus.result)))
                },
                onFailure = { exception ->
                    emit(Result.failure(exception))
                }
            )
        }
    }
}
