package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.getSampleTracks
import com.example.flerkthsound.ui.theme.*

@Composable
fun PlaylistScreen(
//    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val tracks = remember { getSampleTracks() }

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
//            IconButton(onClick = onBackClick) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
//            }

            Text(
                "My Playlist",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Заголовок плейлиста
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎵", fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Favorite Tracks",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DarkTextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "${tracks.size} tracks • Created recently",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Play all */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play All")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Список треков
            items(tracks) { track ->
                SimplePlaylistItem(
                    track = track,
                    onTrackClick = { onTrackClick(track) }
                )
            }
        }
    }
}

@Composable
fun SimplePlaylistItem(
    track: Track,
    onTrackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = DarkCard,
        shape = RoundedCornerShape(8.dp),
        onClick = onTrackClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${track.id}.",
                style = MaterialTheme.typography.bodyMedium,
                color = Silver,
                modifier = Modifier.width(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    track.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    track.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = PrimaryGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                "${track.duration / 60}:${String.format("%02d", track.duration % 60)}",
                color = Silver,
                fontSize = 12.sp
            )
        }
    }
}