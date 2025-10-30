package com.example.flerkthsound.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "flerkthsound_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_ID_KEY = "user_id"
    private const val USER_ROLE_KEY = "user_role"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String, userId: String? = null, userRole: String? = null) {
        with(getPreferences(context).edit()) {
            putString(TOKEN_KEY, token)
            userId?.let { putString(USER_ID_KEY, it) }
            userRole?.let { putString(USER_ROLE_KEY, it) }
            apply()
        }
        println("💾 Token saved: ${token.take(10)}..., userId: $userId, role: $userRole")
    }

    // ДОБАВЬТЕ ЭТУ ФУНКЦИЮ
    fun saveUserData(context: Context, userId: String, role: String) {
        with(getPreferences(context).edit()) {
            putString(USER_ID_KEY, userId)
            putString(USER_ROLE_KEY, role)
            apply()
        }
        println("💾 User data saved: userId=$userId, role=$role")
    }

    fun getToken(context: Context): String? {
        val token = getPreferences(context).getString(TOKEN_KEY, null)
        println("🔍 Token retrieved: ${token?.take(10)}...")
        return token
    }

    fun getUserId(context: Context): String? {
        val userId = getPreferences(context).getString(USER_ID_KEY, null)
        println("🔍 UserId retrieved: $userId")
        return userId
    }

    fun getUserRole(context: Context): String? {
        val role = getPreferences(context).getString(USER_ROLE_KEY, null)
        println("🔍 UserRole retrieved: $role")
        return role
    }

    fun clearToken(context: Context) {
        getPreferences(context).edit().clear().apply()
        println("🧹 Token cleared")
    }

    fun isLoggedIn(context: Context): Boolean {
        val isLoggedIn = getToken(context) != null
        println("🔐 isLoggedIn: $isLoggedIn")
        return isLoggedIn
    }
}


//package com.example.flerkthsound.utils
//
//import android.content.Context
//import android.content.SharedPreferences
//
//object TokenManager {
//    private const val PREFS_NAME = "flerkthsound_prefs"
//    private const val TOKEN_KEY = "jwt_token"
//    private const val USER_ID_KEY = "user_id"
//    private const val USER_ROLE_KEY = "user_role"
//
//    private fun getPreferences(context: Context): SharedPreferences {
//        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    }
//
//    fun saveToken(context: Context, token: String, userId: String? = null, userRole: String? = null) {
//        with(getPreferences(context).edit()) {
//            putString(TOKEN_KEY, token)
//            userId?.let { putString(USER_ID_KEY, it) }
//            userRole?.let { putString(USER_ROLE_KEY, it) }
//            apply()
//        }
//    }
//
//    fun getToken(context: Context): String? {
//        return getPreferences(context).getString(TOKEN_KEY, null)
//    }
//
//    fun getUserId(context: Context): String? {
//        return getPreferences(context).getString(USER_ID_KEY, null)
//    }
//
//    fun getUserRole(context: Context): String? {
//        return getPreferences(context).getString(USER_ROLE_KEY, null)
//    }
//
//    fun clearToken(context: Context) {
//        getPreferences(context).edit().clear().apply()
//    }
//
//    fun isLoggedIn(context: Context): Boolean {
//        return getToken(context) != null
//    }
//}