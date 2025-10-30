package com.example.flerkthsound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCard(
    track: Track,
    onTrackClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = DarkCard,
        shape = RoundedCornerShape(16.dp),
        onClick = onTrackClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Обложка трека
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TitanBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", style = MaterialTheme.typography.headlineSmall)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о треке
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    track.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Статистика
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("♥ ${track.likes}", color = Silver, fontSize = 12.sp)
                    Text("💬 ${track.comments}", color = Silver, fontSize = 12.sp)
                    Text("⏱ ${track.duration / 60}:${String.format("%02d", track.duration % 60)}",
                        color = Silver, fontSize = 12.sp)
                }
            }

            // Кнопка лайка
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (track.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (track.isLiked) SoftPink else Silver
                )
            }
        }
    }
}