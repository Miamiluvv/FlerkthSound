package com.example.flerkthsound.network.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val email: String,
    val name: String,
    val bio: String = "",

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    val role: String = "LISTENER",

    @SerializedName("is_verified")
    val isVerified: Boolean = false, // Изменено на Boolean

    val followers: Int = 0,
    val following: Int = 0,

    @SerializedName("created_at")
    val createdAt: String? = null
)

enum class UserRole {
    GUEST, LISTENER, ARTIST, MODERATOR, ADMIN
}