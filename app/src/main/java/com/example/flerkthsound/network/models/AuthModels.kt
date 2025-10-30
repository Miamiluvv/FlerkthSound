package com.example.flerkthsound.network.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String = "LISTENER"
)

data class CommentRequest(val text: String)