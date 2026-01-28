package com.example.kobac_app.data.api

import com.example.kobac_app.data.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // 로그인 API는 토큰이 필요 없으므로 제외
        if (request.url.encodedPath.contains("/auth/login")) {
            return chain.proceed(request)
        }
        
        // 저장된 토큰을 헤더에 추가
        val token = TokenManager.getToken()
        val newRequest = if (token != null && TokenManager.isTokenValid()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        
        return chain.proceed(newRequest)
    }
}
