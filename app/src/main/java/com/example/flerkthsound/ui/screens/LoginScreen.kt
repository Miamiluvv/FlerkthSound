package com.example.flerkthsound.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flerkthsound.ui.theme.*
import com.example.flerkthsound.viewmodels.AuthState
import com.example.flerkthsound.viewmodels.AuthViewModel

// Добавьте нежно-розовый цвет в вашу палитру
val SoftPinkText = Color(0xFFFFB6C1) // Нежно-розовый цвет для текста
val PrimaryGreen = Color(0xFF4CAF50)
val SoftPink3 = Color(0xFFFFD1DC) // Light Hot Pink
val SoftPink4 = Color(0xFFFF69B4) // Hot Pink


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginAsGuest: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val loginState by authViewModel.loginState.collectAsState()

    // Отладочная информация
    println("🔍 LoginScreen recomposed, loginState: $loginState")

    // Обработка состояний логина
    LaunchedEffect(loginState) {
        println("🔍 LoginScreen LaunchedEffect, loginState: $loginState")
        when (loginState) {
            is AuthState.Success -> {
                println("🎉 LoginScreen: AuthState.Success detected, calling onLoginSuccess")
                onLoginSuccess()
                authViewModel.resetLoginState()
            }
            is AuthState.Error -> {
                println("❌ LoginScreen: AuthState.Error: ${(loginState as AuthState.Error).message}")
            }
            else -> {
                println("🔍 LoginScreen: Other state: $loginState")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Логотип
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(PrimaryGreen, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🎵", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "FlerkthSound",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Text(
            text = "Your Music Community",
            fontSize = 16.sp,
            color = Silver,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Поле email с нежно-розовым текстом
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    "Email",
                    color = SoftPink3 // Нежно-розовый лейбл
                )
            },
            placeholder = {
                Text(
                    "your@email.com",
                    color = SoftPink3.copy(alpha = 0.7f) // Полупрозрачный нежно-розовый
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    tint = SoftPink3 // Нежно-розовая иконка
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPink3.copy(alpha = 0.8f),
                focusedBorderColor = PrimaryGreen,
                cursorColor = SoftPink3,
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = SoftPink3
            ),
            textStyle = LocalTextStyle.current.copy(color = SoftPink3)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле пароля с нежно-розовым текстом
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    "Password",
                    color = SoftPink3 // Нежно-розовый лейбл
                )
            },
            placeholder = {
                Text(
                    "Enter your password",
                    color = SoftPink3.copy(alpha = 0.7f) // Полупрозрачный нежно-розовый
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = SoftPink3 // Нежно-розовая иконка
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPink3.copy(alpha = 0.8f),
                focusedBorderColor = PrimaryGreen,
                cursorColor = SoftPink3, // Нежно-розовый курсор
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = SoftPink3
            ),
            textStyle = LocalTextStyle.current.copy(color = SoftPink3)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка входа
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.login(context, email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            enabled = loginState !is com.example.flerkthsound.viewmodels.AuthState.Loading
        ) {
            if (loginState is com.example.flerkthsound.viewmodels.AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = androidx.compose.ui.graphics.Color.White
                )
            } else {
                Text(
                    "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка гостя
        OutlinedButton(
            onClick = onLoginAsGuest,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SoftPink3,
                contentColor = PrimaryGreen // Нежно-розовый текст кнопки
            )
        ) {
            Text("Explore as Guest")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ссылка на регистрацию
        TextButton(onClick = onNavigateToRegister) {
            Text(
                "Don't have an account? Sign Up",
                color = Silver // Нежно-розовый текст ссылки
            )
        }

        // В LoginScreen добавьте после основных кнопок:
        if (loginState is AuthState.Success) {
            Button(
                onClick = {
                    println("🔄 Manual navigation to feed")
                    onLoginSuccess()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("MANUAL NAVIGATION TO FEED")
            }
        }
    }
}

