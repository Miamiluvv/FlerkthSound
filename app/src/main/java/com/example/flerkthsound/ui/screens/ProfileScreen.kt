package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.network.models.getSampleTracks
import com.example.flerkthsound.ui.theme.DarkBackground
import com.example.flerkthsound.ui.theme.DarkSurface
import com.example.flerkthsound.ui.theme.DarkTextPrimary
import com.example.flerkthsound.ui.theme.PrimaryGreen
import com.example.flerkthsound.ui.theme.Silver
import com.example.flerkthsound.ui.theme.SoftPink
import com.example.flerkthsound.ui.theme.TitanBlue

@Composable
fun ProfileScreen(
    currentUser: User?,
//    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val tracks = remember { getSampleTracks().take(3) }
    val userStats = remember {
        mapOf(
            "Tracks" to "0",
            "Followers" to "0",
            "Following" to "0",
            "Playlists" to "0"
        )
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
//            IconButton(onClick = onBackClick) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
//            }

            Text(
                "Profile",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onEditProfile) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TitanBlue)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Заголовок профиля
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Аватар
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(SoftPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            currentUser?.name?.take(2)?.uppercase() ?: "US",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        currentUser?.name ?: "User",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DarkTextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "@${currentUser?.name?.lowercase()?.replace(" ", "") ?: "user"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryGreen
                    )

                    Text(
                        if (currentUser?.role == "GUEST") "Guest Mode - Limited Access"
                        else "Music lover 🎵 | Creating beats and sharing vibes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Статистика
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        userStats.forEach { (key, value) ->
                            StatItem(title = key, value = value)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Кнопка редактирования (не для гостя)
                    if (currentUser?.role != "GUEST") {
                        Button(
                            onClick = onEditProfile,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TitanBlue)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit Profile")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка выхода
                        OutlinedButton(
                            onClick = onLogout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = SoftPink
                            )
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Logout")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Последние треки (только для артистов и не гостей)
            if (currentUser?.role == "ARTIST" && currentUser.role != "GUEST") {
                item {
                    Text(
                        "Latest Tracks",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkTextPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }

                items(tracks) { track ->
                    ProfileTrackItem(
                        track = track,
                        onTrackClick = { onTrackClick(track) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTrackItem(track: Track, onTrackClick: () -> Unit) {
    Card(
        onClick = onTrackClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
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
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о треке
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    track.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkTextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "${track.duration ?: 180}s • ${track.playsCount ?: 0} plays",
                    style = MaterialTheme.typography.bodySmall,
                    color = Silver
                )
            }

            // Кнопка воспроизведения
            IconButton(onClick = onTrackClick) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play track",
                    tint = PrimaryGreen
                )
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold
        )
        Text(
            title,
            style = MaterialTheme.typography.bodySmall,
            color = Silver,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}