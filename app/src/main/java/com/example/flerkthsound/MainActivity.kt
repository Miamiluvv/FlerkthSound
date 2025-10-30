package com.example.flerkthsound
import com.example.flerkthsound.ui.screens.ProfileScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.flerkthsound.network.models.Track
import com.example.flerkthsound.network.models.User
import com.example.flerkthsound.network.models.getSampleTracks
import com.example.flerkthsound.ui.components.BottomNavigationBar
import com.example.flerkthsound.ui.screens.*
import com.example.flerkthsound.ui.theme.FlerkthSoundTheme
import com.example.flerkthsound.utils.TokenManager
import com.example.flerkthsound.viewmodels.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlerkthSoundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlerkthSoundApp()
                }
            }
        }
    }
}

@Composable
fun FlerkthSoundApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var currentTrack by remember { mutableStateOf<Track?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var playerPosition by remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()


    val showBottomNav = when (currentScreen) {
        "feed", "search", "library", "profile" -> true
        else -> false
    }

    LaunchedEffect(authViewModel.loginState) {
        authViewModel.loginState.collect { state ->
            println("🔍 Login state changed: $state")
            when (state) {
                is com.example.flerkthsound.viewmodels.AuthState.Success -> {
                    println("🎉 Login successful, navigating to feed")
                    val user = state.user
                    currentUser = user
                    user.id?.let { userId ->
                        TokenManager.saveUserData(context, userId, user.role ?: "LISTENER")
                    }
                    currentScreen = "feed"
                    authViewModel.resetLoginState()
                }
                else -> {}
            }
        }
    }


    LaunchedEffect(authViewModel.registerState) {
        authViewModel.registerState.collect { state ->
            println("🔍 Register state changed: $state")
            when (state) {
                is com.example.flerkthsound.viewmodels.AuthState.Success -> {
                    println("🎉 Registration successful, navigating to feed")
                    val user = state.user
                    currentUser = user

                    user.id?.let { userId ->
                        TokenManager.saveUserData(context, userId, user.role ?: "LISTENER")
                    }
                    currentScreen = "feed"
                    authViewModel.resetRegisterState()
                }
                else -> {}
            }
        }
    }

    fun navigateTo(screen: String) {
        println("🔄 Navigating to: $screen")
        currentScreen = screen
    }

    val logout: () -> Unit = {
        println("🚪 Logging out...")
        TokenManager.clearToken(context)
        currentUser = null
        authViewModel.logout(context)
        navigateTo("login")
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigationSelected = { screen ->
                        println("📱 Bottom nav selected: $screen")
                        navigateTo(screen)
                    }
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                "splash" -> SplashScreen { isLoggedIn ->
                    println("🔍 SplashScreen callback: isLoggedIn=$isLoggedIn")
                    if (isLoggedIn && TokenManager.isLoggedIn(context)) {
                        println("🔑 User is logged in, navigating to feed")
                        currentUser = User(
                            id = TokenManager.getUserId(context) ?: "",
                            email = "",
                            name = "User",
                            role = TokenManager.getUserRole(context) ?: "LISTENER"
                        )
                        navigateTo("feed")
                    } else {
                        println("🔐 User not logged in, navigating to login")
                        navigateTo("login")
                    }
                }

                "login" -> LoginScreen(
                    onLoginSuccess = {
                        println("🔄 LoginScreen: onLoginSuccess called")
                    },
                    onNavigateToRegister = {
                        println("🔄 Navigating to register")
                        navigateTo("register")
                    },
                    onLoginAsGuest = {
                        println("👤 Logging in as guest")
                        currentUser = User(
                            id = "guest",
                            email = "guest@flerkthsound.com",
                            name = "Guest",
                            role = "GUEST"
                        )
                        navigateTo("feed")
                    }
                )

                "register" -> RegisterScreen(
                    onRegisterSuccess = {
                        println("🔄 RegisterScreen: onRegisterSuccess called")
                    },
                    onNavigateToLogin = {
                        println("🔄 Navigating to login")
                        navigateTo("login")
                    }
                )

                "feed" -> FeedScreen(
                    currentUser = currentUser,
                    onTrackClick = { track ->
                        println("🎵 Track clicked: ${track.title}")
                        currentTrack = track
                        navigateTo("player")
                    }
                )

                "search" -> SearchScreen(
                    currentUser = currentUser,
                    onTrackClick = { track ->
                        println("🎵 Track clicked: ${track.title}")
                        currentTrack = track
                        navigateTo("player")
                    }
                )

                "library" -> LibraryScreen(
                    currentUser = currentUser,
                    onPlaylistClick = {
                        println("📁 Playlist clicked")
                        navigateTo("playlist")
                    },
                    onTrackClick = { track ->
                        println("🎵 Track clicked: ${track.title}")
                        currentTrack = track
                        navigateTo("player")
                    }
                )

                "profile" -> ProfileScreen(
                    currentUser = currentUser,
                    onTrackClick = { track ->
                        println("🎵 Track clicked: ${track.title}")
                        currentTrack = track
                        navigateTo("player")
                    },
                    onEditProfile = { /* TODO */ },
                    onLogout = {
                        println("🚪 Logout from profile")
                        logout()
                    }
                )

                "player" -> currentTrack?.let { track ->
                    PlayerScreen(
                        currentTrack = track,
                        currentUser = currentUser,
                        isPlaying = isPlaying,
                        currentPosition = playerPosition,
                        onPlayPause = {
                            isPlaying = !isPlaying
                            println("⏯️ Play/Pause: $isPlaying")
                        },
                        onNext = {
                            val tracks = getSampleTracks()
                            val currentIndex = tracks.indexOfFirst { it.id == track.id }
                            val nextIndex = (currentIndex + 1) % tracks.size
                            currentTrack = tracks[nextIndex]
                            playerPosition = 0f
                            println("⏭️ Next track: ${tracks[nextIndex].title}")
                        },
                        onPrevious = {
                            val tracks = getSampleTracks()
                            val currentIndex = tracks.indexOfFirst { it.id == track.id }
                            val prevIndex = (currentIndex - 1 + tracks.size) % tracks.size
                            currentTrack = tracks[prevIndex]
                            playerPosition = 0f
                            println("⏮️ Previous track: ${tracks[prevIndex].title}")
                        },
                        onLike = {
                            currentTrack = track.copy(isLiked = !track.isLiked)
                            println("❤️ Like: ${!track.isLiked}")
                        },
                        onClose = {
                            println("❌ Close player")
                            navigateTo("feed")
                        }
                    )
                }

                "playlist" -> PlaylistScreen(
                    onTrackClick = { track ->
                        println("🎵 Track clicked: ${track.title}")
                        currentTrack = track
                        navigateTo("player")
                    }
                )
            }
        }
    }
}

