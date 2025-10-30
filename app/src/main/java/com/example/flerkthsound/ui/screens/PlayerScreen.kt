package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.ui.theme.*

@Composable
fun PlayerScreen(
    currentTrack: Track,
    currentUser: User?,
    isPlaying: Boolean,
    currentPosition: Float,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onLike: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя панель
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Close", tint = Silver)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("NOW PLAYING", color = Silver, fontSize = 12.sp)
                    Text("FlerkthSound", color = PrimaryGreen, fontSize = 14.sp)
                }

                IconButton(onClick = { /* Menu */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Silver)
                }
            }

            // Центральная часть
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Обложка
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(TitanBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎵", fontSize = 80.sp)
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Информация о треке
                Text(
                    currentTrack.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Text(
                    currentTrack.artist,
                    fontSize = 16.sp,
                    color = PrimaryGreen,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Прогресс-бар
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = currentPosition,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = PrimaryGreen,
                        trackColor = Silver.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0:00", color = Silver, fontSize = 12.sp)
                        Text(
                            "${currentTrack.duration / 60}:${String.format("%02d", currentTrack.duration % 60)}",
                            color = Silver,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Нижняя часть - управление
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Основное управление
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onPrevious) {
                        Text("⏮", fontSize = 24.sp, color = DarkTextPrimary)
                    }

                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier
                            .size(64.dp)
                            .background(PrimaryGreen, CircleShape)
                    ) {
                        Text(
                            if (isPlaying) "⏸" else "▶",
                            fontSize = 24.sp,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }

                    IconButton(onClick = onNext) {
                        Text("⏭", fontSize = 24.sp, color = DarkTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Действия
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (currentUser?.role != "GUEST") onLike()
                        },
                        enabled = currentUser?.role != "GUEST"
                    ) {
                        Icon(
                            imageVector = if (currentTrack.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (currentTrack.isLiked) SoftPink else Silver
                        )
                    }

                    IconButton(
                        onClick = { /* Add to playlist */ },
                        enabled = currentUser?.role != "GUEST"
                    ) {
                        Text("➕", fontSize = 20.sp, color = Silver)
                    }

                    IconButton(onClick = { /* Share */ }) {
                        Text("↗", fontSize = 20.sp, color = Silver)
                    }
                }
            }
        }
    }
}