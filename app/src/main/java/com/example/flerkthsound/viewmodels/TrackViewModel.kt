package com.example.flerkthsound.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.flerkthsound.network.RetrofitInstance
import com.example.flerkthsound.network.models.Track

class TrackViewModel : ViewModel() {

    private val _tracksState = MutableStateFlow<TracksState>(TracksState.Loading)
    val tracksState: StateFlow<TracksState> = _tracksState

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    fun loadFeed(context: Context) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitInstance.createApiService(context)
                val response = apiService.getFeed()

                if (response.isSuccessful && response.body()?.success == true) {
                    _tracksState.value = TracksState.Success(response.body()!!.data ?: emptyList())
                } else {
                    _tracksState.value = TracksState.Error("Failed to load tracks")
                }
            } catch (e: Exception) {
                _tracksState.value = TracksState.Error("Network error: ${e.message}")
            }
        }
    }

    fun setCurrentTrack(track: Track?) {
        _currentTrack.value = track
    }

    fun likeTrack(context: Context, trackId: String) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitInstance.createApiService(context)
                apiService.likeTrack(trackId)
                // Обновляем состояние лайка локально
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}

sealed class TracksState {
    object Loading : TracksState()
    data class Success(val tracks: List<Track>) : TracksState()
    data class Error(val message: String) : TracksState()
}