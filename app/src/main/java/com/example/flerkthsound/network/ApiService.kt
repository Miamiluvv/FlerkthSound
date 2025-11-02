package com.example.flerkthsound.network

import com.example.flerkthsound.network.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface FlerkthSoundApiService {

    // Аутентификация
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<LoginResponse>>

    // Пользователи
    @GET("users/profile")
    suspend fun getProfile(): Response<ApiResponse<User>>

    @PUT("users/profile")
    suspend fun updateProfile(@Body user: User): Response<ApiResponse<User>>

    @Multipart
    @POST("users/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): Response<ApiResponse<User>>

    // Треки
    @GET("tracks/feed")
    suspend fun getFeed(): Response<ApiResponse<List<Track>>>

    @GET("tracks/{trackId}")
    suspend fun getTrack(@Path("trackId") trackId: String): Response<ApiResponse<Track>>

    @Multipart
    @POST("tracks/upload")
    suspend fun uploadTrack(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part audioFile: MultipartBody.Part,
        @Part coverArt: MultipartBody.Part?
    ): Response<ApiResponse<Track>>

    // Лайки
    @POST("tracks/{trackId}/like")
    suspend fun likeTrack(@Path("trackId") trackId: String): Response<ApiResponse<Unit>>

    @DELETE("tracks/{trackId}/like")
    suspend fun unlikeTrack(@Path("trackId") trackId: String): Response<ApiResponse<Unit>>

    // Комментарии
    @GET("tracks/{trackId}/comments")
    suspend fun getComments(@Path("trackId") trackId: String): Response<ApiResponse<List<Comment>>>

    @POST("tracks/{trackId}/comments")
    suspend fun addComment(
        @Path("trackId") trackId: String,
        @Body request: CommentRequest
    ): Response<ApiResponse<Comment>>

    // Поиск
    @GET("search")
    suspend fun search(@Query("q") query: String): Response<ApiResponse<SearchResponse>>
}

data class SearchResponse(
    val tracks: List<Track>,
    val users: List<User>,
    val playlists: List<Playlist>
)