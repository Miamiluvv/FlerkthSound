package com.example.flerkthsound.network.models

data class Playlist(
    val id: String,
    val title: String,
    val description: String = "",
    val ownerId: String,
    val coverUrl: String? = null,
    val trackCount: Int = 0,
    val isPublic: Boolean = true,
    val createdAt: String? = null
)