package com.example.flerkthsound.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.ui.components.TrackCard
import com.example.flerkthsound.ui.theme.*
import com.example.flerkthsound.viewmodels.TrackViewModel

@Composable
fun FeedScreen(
    currentUser: User?,
    onTrackClick: (com.example.flerkthsound.network.models.Track) -> Unit
) {
    val context = LocalContext.current
    val trackViewModel: TrackViewModel = viewModel()
    val tracksState by trackViewModel.tracksState.collectAsState()

    // Загружаем ленту при открытии экрана
    LaunchedEffect(Unit) {
        trackViewModel.loadFeed(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "FlerkthSound",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Аватар пользователя
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(SoftPink, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    currentUser?.name?.take(1) ?: "U",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Контент
        when (tracksState) {
            is com.example.flerkthsound.viewmodels.TracksState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is com.example.flerkthsound.viewmodels.TracksState.Success -> {
                val tracks = (tracksState as com.example.flerkthsound.viewmodels.TracksState.Success).tracks
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .background(DarkBackground),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Приветственный баннер
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(100.dp)
                                .background(PrimaryGreen, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "Добро пожаловать, ${currentUser?.name ?: "Гость"}!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                                Text(
                                    "Слушайте новую музыку",
                                    fontSize = 14.sp,
                                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Заголовок ленты
                    item {
                        Text(
                            "Рекомендуем послушать",
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkTextPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // Список треков
                    items(tracks) { track ->
                        TrackCard(
                            track = track,
                            onTrackClick = {
                                if (currentUser?.role != "GUEST") {
                                    onTrackClick(track)
                                }
                            },
                            onLikeClick = {
                                if (currentUser?.role != "GUEST") {
                                    trackViewModel.likeTrack(context, track.id)
                                }
                            }
                        )
                    }
                }
            }
            is com.example.flerkthsound.viewmodels.TracksState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Ошибка загрузки ленты",
                        color = DarkTextPrimary
                    )
                }
            }
        }
    }
}
