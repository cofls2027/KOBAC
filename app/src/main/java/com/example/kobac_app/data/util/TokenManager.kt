package com.example.kobac_app.data.util

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "kobac_app_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_TOKEN_EXPIRY = "token_expiry"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"
    
    private var prefs: SharedPreferences? = null
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveToken(accessToken: String, expiresInSec: Int) {
        prefs?.edit()?.apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            // 현재 시간 + 만료 시간(초)을 밀리초로 변환하여 저장
            putLong(KEY_TOKEN_EXPIRY, System.currentTimeMillis() + (expiresInSec * 1000L))
            apply()
        }
    }
    
    fun getToken(): String? {
        return prefs?.getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun isTokenValid(): Boolean {
        val expiry = prefs?.getLong(KEY_TOKEN_EXPIRY, 0) ?: 0
        return expiry > System.currentTimeMillis()
    }
    
    fun saveUser(userId: String, email: String, name: String) {
        prefs?.edit()?.apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            apply()
        }
    }
    
    fun getUserId(): String? {
        return prefs?.getString(KEY_USER_ID, null)
    }
    
    fun getUserEmail(): String? {
        return prefs?.getString(KEY_USER_EMAIL, null)
    }
    
    fun getUserName(): String? {
        return prefs?.getString(KEY_USER_NAME, null)
    }
    
    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}
