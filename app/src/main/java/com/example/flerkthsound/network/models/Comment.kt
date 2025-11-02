package com.example.flerkthsound.network.models

data class Comment(
    val id: String,
    val userId: String,
    val user: User? = null,
    val trackId: String,
    val text: String,
    val createdAt: String
)