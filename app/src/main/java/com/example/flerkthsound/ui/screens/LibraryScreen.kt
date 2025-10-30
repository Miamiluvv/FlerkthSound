package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.network.models.getSampleTracks
import com.example.flerkthsound.ui.components.TrackCard
import com.example.flerkthsound.ui.theme.*

// Состояния для навигации внутри библиотеки
sealed class LibrarySection {
    object Main : LibrarySection()
    object LikedSongs : LibrarySection()
    object Playlists : LibrarySection()
    object Albums : LibrarySection()
    object YourUploads : LibrarySection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    currentUser: User?,
    onPlaylistClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    var currentSection by remember { mutableStateOf<LibrarySection>(LibrarySection.Main) }

    // В зависимости от текущего раздела показываем соответствующий контент
    when (currentSection) {
        is LibrarySection.Main -> {
            LibraryMainScreen(
                currentUser = currentUser,
                onSectionSelected = { section ->
                    currentSection = section
                },
                onTrackClick = onTrackClick
            )
        }
        is LibrarySection.LikedSongs -> {
            LikedSongsScreen(
                currentUser = currentUser,
                onBackClick = { currentSection = LibrarySection.Main },
                onTrackClick = onTrackClick
            )
        }
        is LibrarySection.Playlists -> {
            PlaylistsScreen(
                currentUser = currentUser,
                onBackClick = { currentSection = LibrarySection.Main },
                onPlaylistClick = onPlaylistClick
            )
        }
        is LibrarySection.Albums -> {
            AlbumsScreen(
                currentUser = currentUser,
                onBackClick = { currentSection = LibrarySection.Main },
                onTrackClick = onTrackClick
            )
        }
        is LibrarySection.YourUploads -> {
            YourUploadsScreen(
                currentUser = currentUser,
                onBackClick = { currentSection = LibrarySection.Main },
                onTrackClick = onTrackClick
            )
        }
    }
}

@Composable
fun LibraryMainScreen(
    currentUser: User?,
    onSectionSelected: (LibrarySection) -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val librarySections = listOf(
        LibraryItem("Liked Songs", "Your favorite tracks", Icons.Default.Favorite, LibrarySection.LikedSongs),
        LibraryItem("Playlists", "Your created playlists", Icons.Default.PlaylistAdd, LibrarySection.Playlists),
        LibraryItem("Albums", "Saved albums", Icons.Default.Album, LibrarySection.Albums),
        LibraryItem("Your Uploads", "Your uploaded tracks", Icons.Default.CloudUpload, LibrarySection.YourUploads)
    )

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
                "Your Library",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(librarySections) { item ->
                LibrarySectionCard(
                    item = item,
                    onSectionSelected = onSectionSelected
                )
            }

            // Рекомендуемые треки
            item {
                Text(
                    "Recently Played",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkTextPrimary,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }

            items(getSampleTracks().take(3)) { track ->
                TrackCard(
                    track = track,
                    onTrackClick = {
                        if (currentUser?.role != "GUEST") {
                            onTrackClick(track)
                        }
                    },
                    onLikeClick = { /* TODO */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySectionCard(
    item: LibraryItem,
    onSectionSelected: (LibrarySection) -> Unit
) {
    Card(
        onClick = { onSectionSelected(item.section) },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PrimaryGreen, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkTextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Silver
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go to ${item.title}",
                tint = Silver,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}

@Composable
fun LikedSongsScreen(
    currentUser: User?,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val likedTracks = remember { getSampleTracks().filter { it.isLiked } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
            }

            Text(
                "Liked Songs",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        if (likedTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "No liked songs",
                        tint = Silver,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        "No liked songs yet",
                        color = Silver,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        "${likedTracks.size} liked songs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(likedTracks) { track ->
                    TrackCard(
                        track = track,
                        onTrackClick = {
                            if (currentUser?.role != "GUEST") {
                                onTrackClick(track)
                            }
                        },
                        onLikeClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistsScreen(
    currentUser: User?,
    onBackClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    val playlists = listOf(
        PlaylistItem("Workout Mix", "12 tracks", "https://example.com/cover1.jpg"),
        PlaylistItem("Chill Vibes", "8 tracks", "https://example.com/cover2.jpg"),
        PlaylistItem("Road Trip", "15 tracks", "https://example.com/cover3.jpg")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
            }

            Text(
                "Your Playlists",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistCard(
                    playlist = playlist,
                    onClick = onPlaylistClick
                )
            }
        }
    }
}

@Composable
fun AlbumsScreen(
    currentUser: User?,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val albums = listOf(
        AlbumItem("Summer Hits 2024", "Various Artists", "12 tracks"),
        AlbumItem("Midnight Dreams", "Luna Star", "10 tracks"),
        AlbumItem("Electric Pulse", "Neon Wave", "8 tracks")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
            }

            Text(
                "Your Albums",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums) { album ->
                AlbumCard(
                    album = album,
                    onClick = { /* TODO: Navigate to album */ }
                )
            }
        }
    }
}

@Composable
fun YourUploadsScreen(
    currentUser: User?,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val uploadedTracks = remember { getSampleTracks().filter { it.artistId == currentUser?.id } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
            }

            Text(
                "Your Uploads",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (currentUser?.role == "ARTIST" || currentUser?.role == "ADMIN") {
                item {
                    Button(
                        onClick = { /* TODO: Open upload dialog */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload New Track")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uploadedTracks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.CloudUpload,
                                contentDescription = "No uploads",
                                tint = Silver,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                if (currentUser?.role == "ARTIST") "No tracks uploaded yet"
                                else "Upload tracks to see them here",
                                color = Silver,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            } else {
                items(uploadedTracks) { track ->
                    TrackCard(
                        track = track,
                        onTrackClick = { onTrackClick(track) },
                        onLikeClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}

// Модели данных
data class LibraryItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val section: LibrarySection
)

data class PlaylistItem(
    val name: String,
    val trackCount: String,
    val coverUrl: String
)

data class AlbumItem(
    val title: String,
    val artist: String,
    val trackCount: String
)

// Компоненты карточек
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistCard(playlist: PlaylistItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(TitanBlue, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LibraryMusic, contentDescription = "Playlist", tint = androidx.compose.ui.graphics.Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(playlist.name, color = DarkTextPrimary, fontWeight = FontWeight.Medium)
                Text(playlist.trackCount, color = Silver, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCard(album: AlbumItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SoftPink, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Album, contentDescription = "Album", tint = androidx.compose.ui.graphics.Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(album.title, color = DarkTextPrimary, fontWeight = FontWeight.Medium)
                Text("${album.artist} • ${album.trackCount}", color = Silver, fontSize = 14.sp)
            }
        }
    }
}



//package com.example.flerkthsound.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.flerkthsound.network.models.Track
//import com.example.flerkthsound.network.models.User
//import com.example.flerkthsound.network.models.getSampleTracks
//import com.example.flerkthsound.ui.theme.*
//
//@Composable
//fun LibraryScreen(
//    currentUser: User?,
////    onBackClick: () -> Unit,
//    onPlaylistClick: (String) -> Unit,
//    onTrackClick: (Track) -> Unit
//) {
//    val playlists = remember {
//        listOf(
//            PlaylistItem("Liked Songs", "24 tracks", Icons.Default.Favorite, SoftPink),
//            PlaylistItem("Recently Played", "18 tracks", Icons.Default.PlayArrow, TitanBlue),
//            PlaylistItem("Workout Mix", "12 tracks", Icons.Default.PlayArrow, PrimaryGreen),
//            PlaylistItem("Chill Vibes", "15 tracks", Icons.Default.PlayArrow, Color(0xFF9C27B0))
//        )
//    }
//
//    val recentTracks = remember { getSampleTracks().take(5) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(DarkBackground)
//    ) {
//        // TopBar
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(DarkSurface)
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
////            IconButton(onClick = onBackClick) {
////                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkTextPrimary)
////            }
//
//            Text(
//                "Your Library",
//                style = MaterialTheme.typography.titleLarge,
//                color = DarkTextPrimary,
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(DarkBackground)
//                .padding(16.dp)
//        ) {
//            // Плейлисты
//            item {
//                Text(
//                    "Playlists",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = DarkTextPrimary,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//            }
//
//            items(playlists) { playlist ->
//                PlaylistLibraryItem(
//                    playlist = playlist,
//                    onClick = { onPlaylistClick(playlist.name) }
//                )
//            }
//
//            // Недавно прослушанные
//            item {
//                Text(
//                    "Recently Played",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = DarkTextPrimary,
//                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
//                )
//            }
//
//            items(recentTracks) { track ->
//                LibraryTrackItem(
//                    track = track,
//                    onTrackClick = { onTrackClick(track) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun PlaylistLibraryItem(
//    playlist: PlaylistItem,
//    onClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        color = DarkCard,
//        shape = RoundedCornerShape(16.dp),
//        onClick = onClick
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(60.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(playlist.color),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(playlist.icon, contentDescription = playlist.name, tint = Color.White)
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    playlist.name,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = DarkTextPrimary,
//                    fontWeight = FontWeight.Medium
//                )
//                Text(
//                    playlist.description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Silver
//                )
//            }
//
//            Icon(Icons.Default.ArrowBack, contentDescription = "Open", tint = Silver)
//        }
//    }
//}
//
//@Composable
//fun LibraryTrackItem(
//    track: Track,
//    onTrackClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        color = DarkCard,
//        shape = RoundedCornerShape(12.dp),
//        onClick = onTrackClick
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(50.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(TitanBlue),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("🎵", fontSize = 20.sp)
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    track.title,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = DarkTextPrimary
//                )
//                Text(
//                    track.artist,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = PrimaryGreen
//                )
//            }
//
//            Text(
//                "${track.duration / 60}:${String.format("%02d", track.duration % 60)}",
//                color = Silver,
//                fontSize = 12.sp
//            )
//        }
//    }
//}
//
//data class PlaylistItem(
//    val name: String,
//    val description: String,
//    val icon: androidx.compose.ui.graphics.vector.ImageVector,
//    val color: Color
//)