package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flerkthsound.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashEnd: (isLoggedIn: Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000) // Задержка 2 секунды
        onSplashEnd(false) // Пока всегда переходим на логин
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PrimaryGreen, SoftPink, TitanBlue)
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "FlerkthSound",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary
            )

            Text(
                "Your Music Community",
                fontSize = 16.sp,
                color = Silver
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(color = PrimaryGreen)
        }
    }
}