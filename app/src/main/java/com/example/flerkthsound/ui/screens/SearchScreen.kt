package com.example.flerkthsound.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.network.models.getSampleTracks
import com.example.flerkthsound.ui.components.TrackCard
import com.example.flerkthsound.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    currentUser: User?,
    onTrackClick: (Track) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val tracks = remember { getSampleTracks() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // TopBar с поиском
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Search",
                style = MaterialTheme.typography.titleLarge,
                color = DarkTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        // Поле поиска
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search for music, artists...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Silver)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Silver,
                    focusedBorderColor = PrimaryGreen
                ),
                textStyle = LocalTextStyle.current.copy(color = SoftPink3)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(horizontal = 16.dp)
        ) {
            // Результаты поиска
            item {
                Text(
                    "Popular Tracks",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkTextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(tracks) { track ->
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

