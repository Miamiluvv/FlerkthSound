package com.example.flerkthsound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.example.flerkthsound.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val registerState by authViewModel.registerState.collectAsState()
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок
        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Поле имени с нежно-розовым текстом
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    "Full Name",
                    color = SoftPink3
                )
            },
            placeholder = {
                Text(
                    "Enter your name",
                    color = SoftPink3.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPink3.copy(alpha = 0.5f),
                focusedBorderColor = PrimaryGreen,
                cursorColor = SoftPink3,
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = SoftPink3
            ),
            textStyle = LocalTextStyle.current.copy(color = SoftPink3)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле email с нежно-розовым текстом
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    "Email",
                    color = SoftPink3
                )
            },
            placeholder = {
                Text(
                    "your@email.com",
                    color = SoftPink3.copy(alpha = 0.7f)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPinkText.copy(alpha = 0.5f),
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
                    color = SoftPink3
                )
            },
            placeholder = {
                Text(
                    "Create a password",
                    color = SoftPink3.copy(alpha = 0.7f)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPink3.copy(alpha = 0.5f),
                focusedBorderColor = PrimaryGreen,
                cursorColor = SoftPink3,
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = SoftPink3
            ),
            textStyle = LocalTextStyle.current.copy(color = SoftPink3)
        )

        Spacer(modifier = Modifier.height(16.dp))

// Поле подтверждения пароля
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    "Confirm Password",
                    color = SoftPink3
                )
            },
            placeholder = {
                Text(
                    "Repeat your password",
                    color = SoftPink3.copy(alpha = 0.7f)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = SoftPink3.copy(alpha = 0.5f),
                focusedBorderColor = PrimaryGreen,
                cursorColor = SoftPink3,
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = SoftPink3
            ),
            textStyle = LocalTextStyle.current.copy(color = SoftPink3)
        )

// Добавьте проверку совпадения паролей перед регистрацией
        val passwordsMatch = password == confirmPassword
        val isFormValid = name.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                passwordsMatch

// Показываем ошибку если пароли не совпадают
        if (confirmPassword.isNotBlank() && !passwordsMatch) {
            Text(
                "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка регистрации
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.register(context, name, email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ссылка на логин
        TextButton(onClick = onNavigateToLogin) {
            Text(
                "Already have an account? Sign In",
                color = Silver
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegisterScreen(
//    onRegisterSuccess: () -> Unit,
//    onNavigateToLogin: () -> Unit
//) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val context = LocalContext.current
//    val authViewModel: AuthViewModel = viewModel()
//    val registerState by authViewModel.registerState.collectAsState()
//
//    // Обработка состояний регистрации
//    LaunchedEffect(registerState) {
//        when (registerState) {
//            is com.example.flerkthsound.viewmodels.AuthState.Success -> {
//                onRegisterSuccess()
//                authViewModel.resetRegisterState()
//            }
//            is com.example.flerkthsound.viewmodels.AuthState.Error -> {
//                errorMessage = (registerState as com.example.flerkthsound.viewmodels.AuthState.Error).message
//            }
//            else -> {}
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(DarkBackground)
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Логотип
//        Box(
//            modifier = Modifier
//                .size(100.dp)
//                .background(PrimaryGreen, androidx.compose.foundation.shape.CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("🎵", fontSize = 40.sp)
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(
//            "Create Account",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = PrimaryGreen
//        )
//
//        Text(
//            "Join our music community",
//            fontSize = 16.sp,
//            color = Silver,
//            modifier = Modifier.padding(top = 8.dp)
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Показ ошибки
//        errorMessage?.let { message ->
//            Text(
//                message,
//                color = SoftPink,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//        }
//
//        // Поле имени
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Full Name") },
//            leadingIcon = {
//                Icon(Icons.Default.Person, contentDescription = "Name")
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Поле email
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                Icon(Icons.Default.Email, contentDescription = "Email")
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Поле пароля
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            leadingIcon = {
//                Icon(Icons.Default.Lock, contentDescription = "Password")
//            },
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Подтверждение пароля
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            leadingIcon = {
//                Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
//            },
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Кнопка регистрации
//        Button(
//            onClick = {
//                if (name.isBlank() || email.isBlank() || password.isBlank()) {
//                    errorMessage = "Please fill all fields"
//                    return@Button
//                }
//                if (password != confirmPassword) {
//                    errorMessage = "Passwords don't match"
//                    return@Button
//                }
//                if (password.length < 6) {
//                    errorMessage = "Password must be at least 6 characters"
//                    return@Button
//                }
//
//                errorMessage = null
//                authViewModel.register(context, name, email, password)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
//            enabled = registerState !is com.example.flerkthsound.viewmodels.AuthState.Loading
//        ) {
//            if (registerState is com.example.flerkthsound.viewmodels.AuthState.Loading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    color = androidx.compose.ui.graphics.Color.White
//                )
//            } else {
//                Text(
//                    "Sign Up",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Ссылка на вход
//        TextButton(onClick = onNavigateToLogin) {
//            Text("Already have an account? Sign In")
//        }
//    }
//}