package com.example.kobac_app.data.api

import com.example.kobac_app.data.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Content-Type 헤더는 본문(body)이 있는 모든 요청에 필요할 수 있으므로 먼저 처리
        if (originalRequest.body != null && originalRequest.header("Content-Type") == null) {
            requestBuilder.addHeader("Content-Type", "application/json; charset=UTF-8")
        }

        // 2. 로그인 API가 아닌 경우에만 인증 및 트랜잭션 헤더 추가
        if (!originalRequest.url.encodedPath.contains("/auth/login")) {
            // 트랜잭션 ID 헤더 추가
            requestBuilder.addHeader("x-api-tran-id", UUID.randomUUID().toString().replace("-", "").uppercase())

            // 유효한 토큰이 있을 경우, 인증 헤더 추가
            val token = TokenManager.getToken()
            if (token != null && TokenManager.isTokenValid()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
