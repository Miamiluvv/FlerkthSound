package com.example.flerkthsound.network.models

//data class ApiResponse<T>(
//    val success: Boolean,
//    val data: T? = null,
//    val error: String? = null,
//    val token: String? = null,
//    val user: User? = null
//)
//
//// network/models/AuthModels.kt
//data class LoginRequest(
//    val email: String,
//    val password: String
//)
//
//data class LoginResponse(
//    val token: String,
//    val user: User
//)
//
//data class RegisterRequest(
//    val email: String,
//    val password: String,
//    val name: String,
//    val role: String = "LISTENER"
//)
//
//// network/models/User.kt
//data class User(
//    val id: String,
//    val email: String,
//    val name: String,
//    val bio: String = "",
//    val avatarUrl: String? = null,
//    val role: String = "LISTENER",
//    val isVerified: Boolean = false,
//    val followers: Int = 0,
//    val following: Int = 0,
//    val createdAt: String? = null
//)
//

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val token: String? = null,
    val user: User? = null
)