package com.example.flerkthsound.network.models

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val artistId: String,
    val description: String = "",
    val genre: String = "Pop",
    val audioFileUrl: String? = null,
    val coverArtUrl: String? = null,
    val duration: Int = 0,
    val playsCount: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0,
    val isLiked: Boolean = false,
    val isPublic: Boolean = true,
    val createdAt: String? = null
)

// Функция для тестовых данных
fun getSampleTracks(): List<Track> = listOf(
    Track(
        id = "1",
        title = "Лунная ночь",
        artist = "Anna Star",
        artistId = "2",
        likes = 1245,
        comments = 45,
        playsCount = 12500,
        genre = "Pop",
        duration = 225
    ),
    Track(
        id = "2",
        title = "Summer Vibes",
        artist = "DJ Mike",
        artistId = "3",
        likes = 845,
        comments = 23,
        playsCount = 8900,
        genre = "Electronic",
        duration = 252,
        isLiked = true
    ),
    Track(
        id = "3",
        title = "Urban Dreams",
        artist = "City Beats",
        artistId = "4",
        likes = 1567,
        comments = 67,
        playsCount = 15600,
        genre = "Hip-Hop",
        duration = 202
    )
)