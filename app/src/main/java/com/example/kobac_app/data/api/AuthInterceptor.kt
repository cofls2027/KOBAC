package com.example.kobac_app.data.api

import com.example.kobac_app.data.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        
        // 로그인 API는 토큰이 필요 없으므로 제외
        if (!request.url.encodedPath.contains("/users/login/v2")) {
            // 저장된 토큰을 헤더에 추가
            val token = TokenManager.getToken()
            if (token != null && TokenManager.isTokenValid()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        
        // x-api-tran-id 헤더 추가 (필수)
        requestBuilder.addHeader("x-api-tran-id", UUID.randomUUID().toString().replace("-", "").uppercase())
        
        // Content-Type 헤더 추가 (POST 요청인 경우)
        if (request.body != null && request.header("Content-Type") == null) {
            requestBuilder.addHeader("Content-Type", "application/json; charset=UTF-8")
        }
        
        return chain.proceed(requestBuilder.build())
    }
}
